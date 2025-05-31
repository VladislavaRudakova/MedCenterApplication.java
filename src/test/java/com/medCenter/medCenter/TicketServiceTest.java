package com.medCenter.medCenter;

import com.medCenter.medCenter.dto.*;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.repository.ClientRepository;
import com.medCenter.medCenter.model.repository.PersonalJobRepository;
import com.medCenter.medCenter.model.repository.PersonalRepository;
import com.medCenter.medCenter.model.repository.ServiceRepository;
import com.medCenter.medCenter.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Transactional
@SpringBootTest
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PersonalJobService personalJobService;
    @Autowired
    private PersonalRepository personalRepository;
    @Autowired
    private PersonalJobRepository personalJobRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;


    private void createSubInstances() {

    }

    private PersonalJob createSubInstancesPersonalJob() {
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 10;
        String job = "testJob";
        String departmentName = "testDepartment";

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name(departmentName).build();

        departmentService.createDepartment(departmentDto);
        departmentDto = departmentService.findByName(departmentName);

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience)
                .build();

        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .personal(personalDto)
                .jobTitle(job)
                .department(departmentDto).build();

        personalJobService.createPersonalJob(personalJobDto);
//        List<PersonalJobDto> personalJobDtoList = personalJobService.findByNameSurnameJob(name, surname, job);
        List<PersonalJob> personalJobList = personalJobRepository.findByNameSurnameJob(name, surname, job);
        return personalJobList.getLast();
    }

    private PersonalJob createSubInstancesPersonalJob2() {
        String name = "testName1";
        String surname = "testSurname1";
        Date birthDate = Date.valueOf("2000-02-01");
        Date employmentDate = Date.valueOf("2022-02-01");
        Integer experience = 10;
        String job = "testJob1";
        String departmentName = "testDepartment1";

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name(departmentName).build();

        departmentService.createDepartment(departmentDto);
        departmentDto = departmentService.findByName(departmentName);

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience)
                .build();

        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .personal(personalDto)
                .jobTitle(job)
                .department(departmentDto).build();

        personalJobService.createPersonalJob(personalJobDto);
//        List<PersonalJobDto> personalJobDtoList = personalJobService.findByNameSurnameJob(name, surname, job);
        List<PersonalJob> personalJobList = personalJobRepository.findByNameSurnameJob(name, surname, job);
        return personalJobList.getLast();
    }

    private Service createSubInstancesService() {
        Assertions.assertNotNull(serviceRepository);
        Service service = Service.builder()
                .type("testType")
                .price(100.0)
                .build();

        serviceRepository.save(service);

        Service serviceSaved = serviceRepository.findByType("testType");
        return serviceSaved;
    }

    private Client createSubInstancesClient() {
        Client client = Client.builder()
                .name("testName")
                .surname("testSurname")
                .telephoneNumber("12345")
                .build();
        clientRepository.save(client);
        List<Client> clientDtoList = clientRepository.findByNameAndSurname("testName", "testSurname");
        return clientDtoList.getLast();
    }


    @Test
    public void ticketToDto() {

        Assertions.assertNotNull(ticketService);
        Service service = createSubInstancesService();

        PersonalJob personalJob = createSubInstancesPersonalJob();
        Assertions.assertNotNull(personalJob);
        Ticket ticket = Ticket.builder()
                .date(Date.valueOf("2025-05-05"))
                .time(Time.valueOf("11:00:00"))
                .service(service)
                .personalJob(personalJob).build();

        TicketDto ticketDto = ticketService.ticketToDto(ticket);

        Assertions.assertEquals(ticket.getDate().toLocalDate(), ticketDto.getDate());
        Assertions.assertEquals(ticket.getTime().toLocalTime(), ticketDto.getTime());
        Assertions.assertEquals(ticket.getPersonalJob().getId(), ticketDto.getPersonalJob().getId());


    }

    @Test
    public void dtoToTicket() {

        Assertions.assertNotNull(ticketService);
        Service service = createSubInstancesService();
        Assertions.assertNotNull(service);
        ServiceDto serviceDto = serviceService.serviceToDto(service);
        PersonalJob personalJob = createSubInstancesPersonalJob();

        Assertions.assertNotNull(personalJob);

        PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);

        TicketDto ticketDto = TicketDto.builder()
                .service(serviceDto)
                .personalJob(personalJobDto)
                .date(LocalDate.of(2025, 5, 5))
                .time(LocalTime.of(11, 0, 0)).build();

        Ticket ticket = ticketService.dtoToTicket(ticketDto);

        Assertions.assertNotNull(ticket);
        Assertions.assertEquals(ticket.getDate().toLocalDate(), ticketDto.getDate());
        Assertions.assertEquals(ticket.getTime().toLocalTime(), ticketDto.getTime());
        Assertions.assertEquals(ticket.getPersonalJob().getId(), ticketDto.getPersonalJob().getId());
    }

    @Test
    public void findByServiceDateTime() {

        Assertions.assertNotNull(ticketService);
        Service service = createSubInstancesService();
        Assertions.assertNotNull(service);
        ServiceDto serviceDto = serviceService.serviceToDto(service);
        PersonalJob personalJob = createSubInstancesPersonalJob();
        Assertions.assertNotNull(personalJob);
        PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);
        LocalDate date = LocalDate.of(2025, 5, 5);
        LocalTime time1 = LocalTime.of(8, 0, 0);
        LocalTime time2 = LocalTime.of(9, 0, 0);
        LocalTime time3 = LocalTime.of(10, 0, 0);
        LocalTime time4 = LocalTime.of(11, 0, 0);
        LocalTime time5 = LocalTime.of(12, 0, 0);
        TicketDto ticketDto = TicketDto.builder()
                .service(serviceDto)
                .personalJob(personalJobDto)
                .date(date)
                .time(time1).build();
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time2);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time3);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time4);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time5);
        ticketService.createTicket(ticketDto);

        List<PersonalJobDto> ticketDtoList = ticketService.findByServiceDateTime(serviceDto.getType(), Date.valueOf(date), Time.valueOf(time1), Time.valueOf(time3));
        System.out.println("PERSONAL LIST: " + ticketDtoList);
        Assertions.assertEquals(ticketDtoList.size(), 1);
        Assertions.assertEquals(ticketDtoList.getFirst().getId(), personalJob.getId());

    }

    @Test
    public void findByDoctorIdAndTime() {
        Assertions.assertNotNull(ticketService);
        Service service = createSubInstancesService();
        Assertions.assertNotNull(service);
        ServiceDto serviceDto = serviceService.serviceToDto(service);
        PersonalJob personalJob = createSubInstancesPersonalJob();
        Assertions.assertNotNull(personalJob);
        PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);
        LocalDate date = LocalDate.of(2025, 5, 5);
        LocalTime time1 = LocalTime.of(8, 0, 0);
        LocalTime time2 = LocalTime.of(9, 0, 0);
        LocalTime time3 = LocalTime.of(10, 0, 0);
        LocalTime time4 = LocalTime.of(11, 0, 0);
        LocalTime time5 = LocalTime.of(12, 0, 0);
        TicketDto ticketDto = TicketDto.builder()
                .service(serviceDto)
                .personalJob(personalJobDto)
                .date(date)
                .time(time1).build();
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time2);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time3);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time4);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time5);
        ticketService.createTicket(ticketDto);

        TicketDto ticketDtoFound = ticketService.findByDoctorIdAndTime(personalJobDto.getId(), Time.valueOf(time3));
        Assertions.assertNotNull(ticketDtoFound);
        Assertions.assertEquals(ticketDtoFound.getPersonalJob().getId(), personalJobDto.getId());
        Assertions.assertEquals(ticketDtoFound.getTime(), time3);
    }

    @Test
    public void findByClient() {
        Assertions.assertNotNull(ticketService);
        Service service = createSubInstancesService();
        Assertions.assertNotNull(service);

        PersonalJob personalJob = createSubInstancesPersonalJob();
        Assertions.assertNotNull(personalJob);
        Client client = createSubInstancesClient();
        Assertions.assertNotNull(client);

        ServiceDto serviceDto = serviceService.serviceToDto(service);
        PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);
        ClientDto clientDto = clientService.clientToDto(client);


        TicketDto ticketDto = TicketDto.builder()
                .service(serviceDto)
                .personalJob(personalJobDto)
                .client(clientDto)
                .date(LocalDate.of(2025, 5, 5))
                .time(LocalTime.of(11, 0, 0)).build();


        ticketService.createTicket(ticketDto);
        List<TicketDto> ticketDtoList = ticketService.findByClient(client.getId());
        TicketDto ticketDtoFound = ticketDtoList.getLast();
        Assertions.assertNotNull(ticketDtoFound);
        Assertions.assertEquals(ticketDtoFound.getClient().getId(), client.getId());
    }

    @Test
    public void findByState() {
        Assertions.assertNotNull(ticketService);
        Service service = createSubInstancesService();
        Assertions.assertNotNull(service);
        ServiceDto serviceDto = serviceService.serviceToDto(service);
        PersonalJob personalJob = createSubInstancesPersonalJob();
        Assertions.assertNotNull(personalJob);
        PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);
        LocalDate date = LocalDate.of(2025, 5, 5);
        LocalTime time1 = LocalTime.of(8, 0, 0);
        LocalTime time2 = LocalTime.of(9, 0, 0);
        LocalTime time3 = LocalTime.of(10, 0, 0);
        LocalTime time4 = LocalTime.of(11, 0, 0);
        LocalTime time5 = LocalTime.of(12, 0, 0);
        TicketDto ticketDto = TicketDto.builder()
                .service(serviceDto)
                .personalJob(personalJobDto)
                .date(date)
                .time(time1).build();
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time2);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time3);
        ticketDto.setState("testState");
        ticketService.createTicket(ticketDto);

        ticketDto.setTime(time4);
        ticketService.createTicket(ticketDto);
        ticketDto.setTime(time5);
        ticketService.createTicket(ticketDto);

        List<TicketDto> ticketDtoList = ticketService.findByState("testState");
        System.out.println("TICKET LIST: " + ticketDtoList);

        TicketDto ticketDtoFound = ticketDtoList.getLast();
        Assertions.assertNotNull(ticketDtoFound);
        Assertions.assertEquals(ticketDtoList.size(), 3);
        Assertions.assertEquals(ticketDtoFound.getState(), "testState");
    }

    @Test
    public void createTicket() {


    }

    @Test
    public void updateClientAndState() {


    }

    @Test
    public void updateState() {


    }

    @Test
    public void makeAvailable() {

    }

    @Test
    public void findByPersonalJob() {


    }

}
