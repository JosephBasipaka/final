package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import com.example.demo.entity.Customer;
import com.example.demo.entity.Dunning;
import com.example.demo.entity.Service;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.DunningRepository;
import com.example.demo.repositories.RemainderRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.DunningService;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DunningServiceTest {
    
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
    private RemainderRepository reminderRepository;

//     @Test
// public void testSendNotification_Success() throws NoSuchProviderException {
//     // Mock Session and Transport objects
//     Session sessionMock = Mockito.mock(Session.class);
//     Transport transportMock = Mockito.mock(Transport.class);

//     // Mock the behavior of the session
//     Mockito.when(sessionMock.getTransport("smtp")).thenReturn(transportMock);

//     boolean result = dunningService.sendNotification("recipient@example.com", "Test message");

//     assertTrue(result);
// }
//     @Test
//     public void testSendTerminationEmail_Success() throws NoSuchProviderException {
//         // Mock Session and Transport objects
//         Session sessionMock = Mockito.mock(Session.class);
//         Transport transportMock = Mockito.mock(Transport.class);
        
//         // Mock the behavior of the session
//         Mockito.when(sessionMock.getTransport("smtp")).thenReturn(transportMock);
//         // Mockito.when(sessionMock.getProperty("mail.smtp.from")).thenReturn("test@example.com");

//         boolean result = dunningService.sendServiceTerminationEmail("recipient@example.com");

//         assertTrue(result);
// }

@Test
public void testGeneratePdf_Success() throws IOException {
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
    // public void testInitiateDunning_Success() throws IOException {
    //     List<Service> services = new ArrayList<>();
    //     Service service = new Service(1L, "Basic", 1000, "Active", null, null);
    //     services.add(service);
    //     Customer customer = new Customer(1L, "Joseph", "joseph@gmail.com", services, null);
    //     String stepName = "Initial Reminder";

    //     DunningRepository dunningRepository = Mockito.mock(DunningRepository.class);
    // CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    // ServiceRepository serviceRepository = Mockito.mock(ServiceRepository.class);

    // // Create your DunningService instance with the mocked repositories
    //     DunningService dunningService = new DunningService(dunningRepository, customerRepository, serviceRepository);
    //     Dunning dunningStep = new Dunning();
    //     dunningStep.setCustomer(customer);
    //     dunningStep.setService(service);
    //     dunningStep.setStepName(stepName);
    //     dunningStep.setStatus("Pending");
        
    //     dunningService.initiateDunning(customer, service, stepName);

    //     Mockito.when(customerRepository.save(customer)).thenReturn(customer);
    //     Mockito.when(serviceRepository.save(service)).thenReturn(service);
    //     Mockito.when(dunningRepository.save(dunningStep)).thenReturn(dunningStep);



    //     // Verify that the repositories save methods are called
    //     verify(customerRepository, times(1)).save(customer);
    //     verify(serviceRepository, times(1)).save(service);
    //     verify(dunningRepository, times(1)).save(dunningStep);
        
    // }

    @Test
    public void testGenerateReminderMessage() {
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
    public void testIsFinalNoticeSent() {
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
    public void testFindCustomersWithFollowUp() {
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
    public void testFindCustomersWithFinalNotice() {
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
    public void testIsServiceTerminationNeeded() {
        
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

    
}
   

