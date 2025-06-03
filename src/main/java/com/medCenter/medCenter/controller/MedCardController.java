package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.MedCardDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.MedCardService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ServiceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("medCard/")
@RequiredArgsConstructor
public class MedCardController {

    private final MedCardService medCardService;
    private final ClientService clientService;
    private final PersonalJobService personalJobService;
    private final ServiceService serviceService;

    @PostMapping("/seeMedicalCard")
    public String seeMedCard(@RequestParam Integer clientId, HttpSession session, Model model) {
        List<MedCardDto> medCardDtoList = medCardService.findByClientId(clientId); //find medical card
        model.addAttribute("medCards", medCardDtoList);
        ClientDto clientDto = clientService.findByIdDto(clientId);
        session.setAttribute("client", clientDto);
        Set<String> serviceTypes = getServiceTypes(); //get service type list for select med card appointment form
        model.addAttribute("serviceTypes", serviceTypes);
        return "medCardPage";
    }

    private Set<String> getServiceTypes() {
        Set<String> serviceTypes = serviceService.findAllTypes();
        for (String service : serviceTypes) {
            if (isAppointment(service)) {
                service = "doctor appointment";
            }
        }
        return serviceTypes;
    }


    private boolean isAppointment(String serviceType) {
        String regex = "doctor \\w+ appointment";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(serviceType);
        return matcher.find();
    }

    @GetMapping("/getMedCardForm")
    public String makeRecord(Model model, HttpSession session, @RequestParam String serviceType, @AuthenticationPrincipal UserDetailsImpl user) {
        LocalDate localDate = LocalDate.now();
        PersonalJobDto personalJob = personalJobService.findByUserId(user.getUser().getId());
        ServiceDto serviceDto = serviceService.findByType(serviceType);
        session.setAttribute("service", serviceDto);
        session.setAttribute("personalJob", personalJob);
        model.addAttribute("medCard", new MedCardDto());
        model.addAttribute("date", localDate);
        model.addAttribute("personalJob", personalJob);
        return "medCardRecordForm";
    }

    @PostMapping("/makeRecord")
    public String makeRecord(Model model, HttpSession session, @ModelAttribute MedCardDto medCard) {
        medCard.setClient((ClientDto) session.getAttribute("client"));
        medCard.setPersonalJob((PersonalJobDto) session.getAttribute("personalJob"));
        medCard.setService((ServiceDto) session.getAttribute("service"));
        medCard.setDate(LocalDate.now());
        medCardService.createMedCard(medCard);
        MedCardDto medCardDto = medCardService.findByClientIdPersonalJobIdAndDate(medCard.getClient().getId(),
                medCard.getPersonalJob().getId(), Date.valueOf(medCard.getDate())).getLast();
        model.addAttribute("medCardSaved", medCardDto);
        return "createdMedCardRecordPage";
    }

    @PostMapping("/seeRecordDetails")
    public String seeRecordDetails(Model model, HttpSession session, @RequestParam String medCardId) {
        MedCardDto medCardDto = medCardService.findByIdDto(Integer.valueOf(medCardId));
        model.addAttribute("medCard", medCardDto);
        return "detailsMedCardRecordPage";
    }


}
