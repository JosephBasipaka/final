package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.NoSuchProviderException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Dunning;
import com.example.demo.entity.Service;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.DunningRepository;
import com.example.demo.repositories.ReminderRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.DunningService;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class DunningServiceTest {
    
    @InjectMocks
    private DunningService dunningService;

    // @Mock
    // private DunningService dunningServiceMock;

    @Mock
    private CustomerService customerService;
    // @Autowired
    // private ServiceService serviceService;
    @Mock
    private DunningRepository dunningRepository;
    @Mock
    private CustomerRepository customerRepository;
    @MockBean
    private ServiceRepository serviceRepository;
    @Mock
    private ReminderRepository reminderRepository;

    @Test
void testSendNotification_Success() throws NoSuchProviderException {
    // Mock Session and Transport objects
    // Session sessionMock = Mockito.mock(Session.class);
    // Transport transportMock = Mockito.mock(Transport.class);

    // // Mock the behavior of the session
    // Mockito.when(sessionMock.getTransport("smtp")).thenReturn(transportMock);

    boolean result = dunningService.sendNotification("recipient@example.com", "Test message");

    assertTrue(result);
}
    @Test
    void testSendTerminationEmail_Success() throws NoSuchProviderException {
        // Mock Session and Transport objects
        // Session sessionMock = Mockito.mock(Session.class);
        // Transport transportMock = Mockito.mock(Transport.class);
        
        // // Mock the behavior of the session
        // Mockito.when(sessionMock.getTransport("smtp")).thenReturn(transportMock);
        // Mockito.when(sessionMock.getProperty("mail.smtp.from")).thenReturn("recepient@example.com");

        boolean result = dunningService.sendServiceTerminationEmail("recipient@example.com");

        assertTrue(result);
}

@Test
void testGeneratePdf_Success() throws IOException {
    Path tempDir = Files.createTempDirectory("pdf_test");

    Customer customer = new Customer(1L, "Joseph", "joseph@gmail.com", null, null);
    Service service = new Service(1L, "basic", 1000, "Active", null, null);
    String dunningStep = "Initial Reminder";

    dunningService.generatePdf(customer, service, dunningStep, tempDir);

    Path pdfPath = tempDir.resolve("customer_details.pdf");
    assertTrue(Files.exists(pdfPath));

    Files.deleteIfExists(pdfPath);
    Files.deleteIfExists(tempDir);
}

    @BeforeEach
    void setup(){

    }
    // @Test
    // void testInitiateDunning_Success() throws IOException {
    //     List<Service> services = new ArrayList<>();
    //     Service service = new Service(1L, "Basic", 1000, "Active", null, null);
    //     services.add(service);
    //     Customer customer = new Customer(1L, "jo", "jo@example.com", null, null);
    //     String stepName = "Initial Reminder";

    //     Dunning dunningStep = new Dunning();
    //     dunningStep.setCustomer(customer);
    //     dunningStep.setService(service);
    //     dunningStep.setStepName(stepName);
    //     dunningStep.setStatus("Pending");
    //     dunningStep.setTimestamp(new Date());
    //     dunningService.initiateDunning(customer, service, stepName);
    //     verify(dunningRepository, times(1)).save(dunningStep);  
    // }

    @Test
    void testGenerateReminderMessage() {
        Customer customer = new Customer(1L, "Joseph", "joseph@gmail.com", null, null);
        Service service = new Service(1L, "Basic", 1000, "Active", null, null);
        Dunning dunningStep = new Dunning();
        dunningStep.setCustomer(customer);
        dunningStep.setService(service);
        dunningStep.setStepName("Initial Reminder");

        String reminderMessage = dunningService.generateReminderMessage(dunningStep);

        String expectedMessage = "Dear Joseph,\n" +
            "This is a reminder for your service 'Basic'.\n" +
            "Please take action for the 'Initial Reminder' step.";
        assertEquals(expectedMessage, reminderMessage);
    }

    @Test
    void testIsFinalNoticeSent() {
        DunningRepository dunningRepository = Mockito.mock(DunningRepository.class);
        DunningService dunningService = new DunningService(null, null, dunningRepository);
        Customer customer = new Customer(1L, "Joseph", "joseph@gmail.com", null, null);
        Service service = new Service(1L, "Basic", 1000, "Active", null, null);

        Dunning finalNotice = new Dunning();
        finalNotice.setCustomer(customer);
        finalNotice.setService(service);
        finalNotice.setStepName("Final Notice");
        finalNotice.setStatus("Reminder Sent");
        
        Mockito.when(dunningRepository.findByCustomerAndStepNameAndStatusAndService(
            customer,
            "Final Notice",
            "Remainder Sent", 
            service
        )).thenReturn(finalNotice);
            
        boolean isFinalNoticeSent = dunningService.isFinalNoticeSent(customer, service);

        assertTrue(isFinalNoticeSent);
    }

    @Test
    void testFindCustomersWithFollowUp() {
        List<Customer> overdueCustomers = new ArrayList<>();
    
        Customer customer1 = new Customer(1L, "jo", "jo@example.com", null, null);
        Customer customer2 = new Customer(1L, "j", "j@example.com", null, null);
        overdueCustomers.add(customer1);
        overdueCustomers.add(customer2);
        when(customerRepository.findCustomersWithOverdueInvoices()).thenReturn(overdueCustomers);
        when(customerService.isOverdue(any(Customer.class))).thenReturn(true);
        when(reminderRepository.countRemindersSentForCustomer(any(Customer.class))).thenReturn(2L);

        // Call the method to test
        List<Customer> customersWithFollowUp = dunningService.findCustomersWithFollowUp();

        // Assert the results, e.g., check the size of the result list
        assertEquals(2, customersWithFollowUp.size());
    }


    @Test
    void testFindCustomersWithFinalNotice() {
        List<Customer> overdueCustomers = new ArrayList<>();
    
        Customer customer1 = new Customer(1L, "jo", "jo@example.com", null, null);
        Customer customer2 = new Customer(1L, "j", "j@example.com", null, null);
        overdueCustomers.add(customer1);
        overdueCustomers.add(customer2);
        when(customerRepository.findCustomersWithOverdueInvoices()).thenReturn(overdueCustomers);
        for (Customer customer : overdueCustomers) {
        // when(reminderRepository.countRemindersSentForCustomer(customer)).thenReturn(5L);
        doReturn(true).when(customerService).isOverdue(customer);
        doReturn(5L).when(reminderRepository).countRemindersSentForCustomer(customer);
        }
        List<Customer> customersWithFinalNotice = dunningService.findCustomersWithFinalNotice();

        assertEquals(2, customersWithFinalNotice.size());
    }

    @Test
    void testIsServiceTerminationNeeded() {
        
        Customer customer = new Customer(1L, "jo", "jo@example.com", null, null);
        Service service = new Service(1L, "Basic", 1000, "Active", null, null);
        Dunning finalNotice = new Dunning();
        finalNotice.setCustomer(customer);
        finalNotice.setService(service);
        finalNotice.setStepName("Final Notice");
        finalNotice.setStatus("Reminder Sent");
        when(dunningRepository.findDunningStepsForCustomer(customer)).thenReturn(Collections.singletonList(finalNotice));

        boolean serviceTerminationNeeded = dunningService.isServiceTerminationNeeded(customer);

        assertTrue(serviceTerminationNeeded);
    }
     @Test
    void testHandleFinalNotice() {
         Customer customer = new Customer(1L, "jo", "jo@example.com", null, null);
        Service service = new Service(1L, "Basic", 1000, "Active", null, null);
        when(serviceRepository.save(any(Service.class))).thenReturn(service);
        dunningService.handleFinalNotice(customer, service);
        assertEquals("Terminated", service.getStatus());

        verify(serviceRepository, times(1)).save((service));
    }
    @Test
    void testGetTempDirectoryForTestEnvironment() {
        System.setProperty("app.environment", "test");
        DunningService dunningServiceTest = new DunningService(); 
        Path tempDirectory = dunningServiceTest.getTempDirectory();
        assertTrue(tempDirectory.toFile().isDirectory());
        tempDirectory.toFile().deleteOnExit();
    }

    @Test
    void testGetTempDirectoryForDefaultEnvironment() {
        System.clearProperty("app.environment");
        DunningService dunningServiceTest = new DunningService(); 
        Path tempDirectory = dunningServiceTest.getTempDirectory();
        assertEquals("/home/joseph/final", tempDirectory.toString());
    }
}
   

