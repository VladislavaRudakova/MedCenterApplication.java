package com.medCenter.medCenter;


import com.medCenter.medCenter.dto.*;
import com.medCenter.medCenter.model.entity.*;
import com.medCenter.medCenter.model.repository.*;
import com.medCenter.medCenter.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;


@SpringBootTest
@Transactional
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PersonalJobService personalJobService;
    @Autowired
    private PersonalService personalService;
    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PersonalJobRepository personalJobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;

    private User createSubInstanceUser() {

        UserCredentials userCredentials = UserCredentials.builder()
                .login("testLogin")
                .password("testPassword").build();

        User user = User.builder()
                .username("testUsername")
                .role("testRole")
                .email("testMail")
                .userCredentials(userCredentials)
                .build();

        userRepository.save(user);

        user = userService.findByLoginForReg("testLogin");
        return user;
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




    @Test
    public void clientToDto() {

        User user = createSubInstanceUser();
        Assertions.assertNotNull(user);

        Client client = Client.builder()
                .name("testClientName")
                .surname("testClientSurname")
                .telephoneNumber("12345")
                .user(user)
                .build();
        clientRepository.save(client);

        Client clientSaved = clientRepository.findByUserId(user.getId());

        ClientDto clientDto = clientService.clientToDto(clientSaved);
        Assertions.assertNotNull(clientDto);
        Assertions.assertEquals(clientDto.getName(), client.getName());
        Assertions.assertEquals(clientDto.getSurname(), client.getSurname());
        Assertions.assertEquals(clientDto.getTelephoneNumber(), client.getTelephoneNumber());

    }

    @Test
    public void dtoToClient() {

        User user = createSubInstanceUser();
        Assertions.assertNotNull(user);
        System.out.println("USER!!!!!!!!" + user);
        UserDto userDto = userService.userToDto(user);

        ClientDto clientDto = ClientDto.builder()
                .name("testClientName")
                .surname("testClientSurname")
                .telephoneNumber("12345")
                .user(userDto)
                .build();

        Client client = clientService.dtoToClient(clientDto);

        Assertions.assertNotNull(client);
        Assertions.assertEquals(clientDto.getName(), client.getName());
        Assertions.assertEquals(clientDto.getSurname(), client.getSurname());
        Assertions.assertEquals(clientDto.getTelephoneNumber(), client.getTelephoneNumber());


    }

    @Test
    public void createClient() {

        User user = createSubInstanceUser();
        Assertions.assertNotNull(user);
        System.out.println("USER!!!!!!!!" + user);
        UserDto userDto = userService.userToDto(user);

        ClientDto clientDto = ClientDto.builder()
                .name("testClientName")
                .surname("testClientSurname")
                .telephoneNumber("12345")
                .user(userDto)
                .build();

        clientService.createClient(clientDto);
        ClientDto clientDtoSaved = clientService.findByUserId(clientDto.getUser().getId());
        Assertions.assertNotNull(clientDtoSaved);
        Assertions.assertEquals(clientDto.getName(), clientDtoSaved.getName());
        Assertions.assertEquals(clientDto.getSurname(), clientDtoSaved.getSurname());
        Assertions.assertEquals(clientDto.getTelephoneNumber(), clientDtoSaved.getTelephoneNumber());


    }



    @Test
    public void updateState() {
        User user = createSubInstanceUser();
        Assertions.assertNotNull(user);
        System.out.println("USER!!!!!!!!" + user);
        UserDto userDto = userService.userToDto(user);

        ClientDto clientDto = ClientDto.builder()
                .name("testClientName")
                .surname("testClientSurname")
                .telephoneNumber("12345")
                .user(userDto)
                .build();

        clientService.createClient(clientDto);
        ClientDto clientDtoSaved = clientService.findByUserId(clientDto.getUser().getId());
        Assertions.assertNotNull(clientDtoSaved);

        clientService.updateState("testState",clientDtoSaved.getId());

        Client clientDtoUpdated = clientService.findById(clientDtoSaved.getId());
        Assertions.assertNotNull(clientDtoUpdated);
        Assertions.assertEquals(clientDtoSaved.getName(), clientDtoUpdated.getName());
        Assertions.assertEquals(clientDtoSaved.getSurname(), clientDtoUpdated.getSurname());
        Assertions.assertEquals(clientDtoSaved.getTelephoneNumber(), clientDtoUpdated.getTelephoneNumber());
        Assertions.assertNull(clientDtoSaved.getState());
        Assertions.assertEquals(clientDtoUpdated.getState(),"testState");
    }


}
