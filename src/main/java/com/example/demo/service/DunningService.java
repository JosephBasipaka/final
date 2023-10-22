package com.example.demo.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.springframework.beans.factory.annotation.Autowired;


import com.example.demo.entity.Customer;
import com.example.demo.entity.Dunning;
import com.example.demo.entity.Reminder;
import com.example.demo.entity.Service;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.DunningRepository;
import com.example.demo.repositories.ReminderRepository;
import com.example.demo.repositories.ServiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

@org.springframework.stereotype.Service
public class DunningService {
    @Autowired
    private DunningRepository dunningRepository;
    @Autowired
    private CustomerService customerService; 
    @Autowired
    private ServiceService serviceService; 
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ReminderRepository reminderRepository;
    
    private String intialReminder = "Initial Reminder";
    private String followUpReminder = "Follow-Up Reminder";
    private String finalNotice = "Final Notice";
    public DunningService(){
        
    }
    public DunningService(CustomerRepository customerRepository, ServiceRepository serviceRepository, DunningRepository dunningRepository){
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.dunningRepository = dunningRepository;
    }
    public void initiateDunningProcess(Customer customer, Service service, String stepname){
        if(intialReminder.equals(stepname)){
            Dunning existingIntialReminder = dunningRepository.findByCustomerAndStepNameAndStatusAndService(customer, "Initial Reminder", "Reminder Sent", service);
            if(existingIntialReminder == null){
                initiateDunning(customer, service, stepname);
            }
            else System.out.println("Initial Reminder has already been sent for Customer: " + customer.getName() + " and Service: " + service.getServiceName());
        }
        if(followUpReminder.equals(stepname)){
            initiateDunning(customer, service, stepname);
        }
        if(finalNotice.equals(stepname)){
            initiateDunning(customer, service, stepname);
        }
    }
    public void initiateDunning(Customer customer, Service service, String stepName) {
        
        Dunning dunningStep = new Dunning();
        dunningStep.setCustomer(customer);
        dunningStep.setService(service);
        dunningStep.setStepName(stepName);
        dunningStep.setStatus("Pending");
        dunningStep.setTimestamp(new Date());

        System.out.println("Dunning Step (" + stepName + ") started for Customer: " + customer.getName());
        Path tempDir = getTempDirectory();
        try {
            generatePdf(customer, service,stepName,tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendDunningReminder(dunningStep);
        // dunningRepository.save(dunningStep);

        Reminder rem = new Reminder();
        rem.setCustomer(customer);
        rem.setContent(stepName);
        rem.setTimestamp(new Date());
        reminderRepository.save(rem);

        System.out.println("Remainder Step (" + stepName + ") completed for Customer: " + customer.getName());
        System.out.println("Dunning Step (" + stepName + ") completed for Customer: " + customer.getName());
    
    }

    public List<Customer> findCustomersWithFollowUp() {
        List<Customer> customersWithFollowUp = new ArrayList<>();
        List<Customer> overdueCustomers = customerRepository.findCustomersWithOverdueInvoices();
        System.out.println("cus "  + overdueCustomers);
        for (Customer customer : overdueCustomers) {
            if (customerRequiresFollowUp(customer)) {
                customersWithFollowUp.add(customer);
            }
        }
        return customersWithFollowUp;
    }
    public boolean isFinalNoticeSent(Customer customer,Service service) {
        Dunning isfinalNotice = dunningRepository.findByCustomerAndStepNameAndStatusAndService(
            customer,
            finalNotice,
            "Reminder Sent",
            service
        );
        return isfinalNotice != null;
    }
    public List<Customer> findCustomersWithFinalNotice() {
        List<Customer> customersWithFinalNotice = new ArrayList<>();
        List<Customer> overdueCustomers = customerRepository.findCustomersWithOverdueInvoices(); 
        for (Customer customer : overdueCustomers) {
            if (customerRequiresFinalNotice(customer)) {
                customersWithFinalNotice.add(customer);
            }
        }
        return customersWithFinalNotice;
    }

    private boolean customerRequiresFollowUp(Customer customer) {
        int maxReminders = 2;
        int remindersSent = (int) reminderRepository.countRemindersSentForCustomer(customer);
        System.out.println("rem count" + remindersSent);
        return customerService.isOverdue(customer) && remindersSent <= maxReminders;
    }
    private boolean customerRequiresFinalNotice(Customer customer) {
        int maxReminders = 3; 
        int remindersSent = (int) reminderRepository.countRemindersSentForCustomer(customer);
    
        return customerService.isOverdue(customer) && remindersSent >= maxReminders;
    }
    public boolean isServiceTerminationNeeded(Customer customer) {
        List<Dunning> dunningSteps = dunningRepository.findDunningStepsForCustomer(customer);
    
        for (Dunning dunningStep : dunningSteps) {
            if ("Final Notice".equals(dunningStep.getStepName())) {
                // Found a "Final Notice" dunning step for the customer
                return true;
            }
        }
    
        return false; // No "Final Notice" dunning step found
    }

    public void handleFinalNotice(Customer customer, Service service) {
        Service newService = service;
        newService.setStatus("Terminated");
        serviceRepository.save(newService);
        Path tempDir = getTempDirectory();
        try {
            generatePdf(customer, service, "Terminate",tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendServiceTerminationEmail(customer.getEmail());
    }
    
    private void sendDunningReminder(Dunning dunningStep) {
        Customer customer = dunningStep.getCustomer();
        String reminderMessage = generateReminderMessage(dunningStep);
        boolean sentSuccessfully = sendNotification(customer.getEmail(), reminderMessage);
        if (sentSuccessfully) {
            dunningStep.setStatus("Reminder Sent");
            dunningRepository.save(dunningStep);
        } else {
            System.out.println("Remainder not sent");
        }
    }

public boolean sendNotification(String recipientEmail, String messageContent) {
    String senderEmail = "josephbasipaka@gmail.com";
    String senderPassword = "ocyj muvp xedp iyjj ";
    String smtpHost = "smtp.gmail.com";
    // int smtpPort = 587;

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", smtpHost);
    // props.put("mail.smtp.port", smtpPort);

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmail, senderPassword);
        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Dunning Reminder"); 
        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(messageContent);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File("/home/joseph/final/customer_details.pdf"));

        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);
        Transport.send(message);

        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public String generateReminderMessage(Dunning dunningStep) {
    // You can construct the reminder message based on the dunningStep's information
    // For example, you can include the customer's name, service details, and step name
    String customerName = dunningStep.getCustomer().getName();
    String serviceName = dunningStep.getService().getServiceName();
    String stepName = dunningStep.getStepName();


    return "Dear " + customerName + ",\n" +
                            "This is a reminder for your service '" + serviceName + "'.\n" +
                            "Please take action for the '" + stepName + "' step.";
}
public Session createMailSession() {
    String senderEmail = "josephbasipaka@gmail.com";
    String senderPassword = "ocyj muvp xedp iyjj ";
    String smtpHost = "smtp.gmail.com";

    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", smtpHost);
   
    return Session.getInstance(properties, new javax.mail.Authenticator() {
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
        }
    });
}
public boolean sendServiceTerminationEmail(String customerEmail) {
    String senderEmail = "josephbasipaka@gmail.com";
    String senderPassword = "ocyj muvp xedp iyjj ";
    String smtpHost = "smtp.gmail.com";

    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", smtpHost);
   
    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
        }
    });
    
    try {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customerEmail));
        message.setSubject("Service Termination Notification");
        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Your service has been terminated. Please contact us for reactivation.");

        MimeBodyPart attachmentPart = new MimeBodyPart();
        try {
            attachmentPart.attachFile(new File("/home/joseph/final/customer_details.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);
        Transport.send(message);
        return true;
    } catch (MessagingException e) {
        e.printStackTrace();
        return false;
    }
}

    public void generatePdf(Customer customer, Service service,String dunningStep,Path directory) throws IOException{
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            PDImageXObject image = PDImageXObject.createFromFile("/home/joseph/final/src/main/resources/static/images/images.png", document);
            contentStream.drawImage(image, 100, 400, image.getWidth(), image.getHeight());
        }
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Invoice:");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Customer Details:");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Name: " + customer.getName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Email: " + customer.getEmail());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Service Details:");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Name: " + service.getServiceName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Cost: " + service.getServiceCost());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Status: " + service.getStatus());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(dunningStep.equals("Terminate")
            ? "Your service has been terminated. Please contact us for reactivation."
            : "This is a reminder to pay for the service as this is the " + dunningStep);        
            PDAnnotationLink link = new PDAnnotationLink();
            PDRectangle rect = new PDRectangle(200, 1500, 200, 20);
            link.setRectangle(rect);
            PDActionURI action = new PDActionURI();
            action.setURI("http://localhost:3000");
            link.setAction(action);
            contentStream.newLineAtOffset(0, -20);
 
            
            contentStream.showText("Can pay the amount here : http://localhost:3000/payment");
            contentStream.endText();
            page.getAnnotations().add(link);
        }
        // document.save("customer_details.pdf");
        Path pdfPath = directory.resolve("customer_details.pdf");
        document.save(pdfPath.toFile());
        document.close();
    }

    public Path getTempDirectory() {
    String environment = System.getProperty("app.environment");
    if ("test".equalsIgnoreCase(environment)) {
        try {
            return Files.createTempDirectory("pdf_test");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary directory for testing.", e);
        }
    } else {
        return Paths.get("/home/joseph/final");
    }
}

    public List<DunningResponse> getDunningDetails(Long customerId, Long serviceId){
        Customer customer = customerService.getCustomerById(customerId); // Replace with your own service to retrieve a customer
        Service service = serviceService.getService(serviceId);
        List<DunningResponse> dunningResponses = new ArrayList<>();
        
        Dunning initialDunning = dunningRepository.findByCustomerAndStepNameAndStatusAndService(customer, "Initial Reminder", "Reminder Sent",service);
        boolean initialReminder = initialDunning != null;

        // Fetch "Final" data
        Dunning finalDunning = dunningRepository.findByCustomerAndStepNameAndStatusAndService(customer, "Final Notice", "Reminder Sent",service);
        boolean finalReminder = finalDunning != null;

        // Fetch "Follow-Up" data
        long followUpReminderCount = reminderRepository.countRemindersSentForCustomerWithContent(customer,"Follow-Up Reminder");

        DunningResponse initialResponse = new DunningResponse(initialReminder, followUpReminderCount, finalReminder);
        dunningResponses.add(initialResponse);
        return dunningResponses;
    }
}

