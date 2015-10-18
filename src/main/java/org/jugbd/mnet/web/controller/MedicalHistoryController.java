package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.MedicalHistory;
import org.jugbd.mnet.domain.Register;
import org.jugbd.mnet.domain.enums.Gender;
import org.jugbd.mnet.service.MedicalHistoryService;
import org.jugbd.mnet.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Bazlur Rahman Rokon
 * @date 11/27/2014.
 */
@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/medicalhistory")
public class MedicalHistoryController {
    public static final String MEDICAL_HISTORY_CREATE_PAGE = "medicalhistory/create";
    public static final String REDIRECT_REGISTER_MEDICAL_HISTORY_PAGE = "redirect:/register/medicalhistory/";
    public static final String MEDICAL_HISTORY_SHOW_PAGE = "medicalhistory/show";
    public static final String MEDICAL_HISTORY_EDIT_PAGE = "medicalhistory/edit";

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "create/{registerId}", method = RequestMethod.GET)
    public String create(@PathVariable Long registerId, MedicalHistory medicalHistory, Model uiModel) {

        Register register = registerService.findOne(registerId);
        medicalHistory.setRegister(register);

        Gender gender = register.getPatient().getGender();
        boolean isFemale = gender == Gender.FEMALE;
        uiModel.addAttribute("isFemale", isFemale);

        return MEDICAL_HISTORY_CREATE_PAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@Valid MedicalHistory medicalHistory,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return MEDICAL_HISTORY_CREATE_PAGE;
        }

        MedicalHistory saveMedicalHistory = medicalHistoryService.save(medicalHistory);
        redirectAttributes.addFlashAttribute("message", "Medical History successfully created");

        return REDIRECT_REGISTER_MEDICAL_HISTORY_PAGE + saveMedicalHistory.getRegister().getId();
    }

    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id, Model uiModel) {
        uiModel.addAttribute("medicalHistory", medicalHistoryService.findOne(id));

        return MEDICAL_HISTORY_SHOW_PAGE;
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model uiModel) {
        MedicalHistory medicalHistory = medicalHistoryService.findOne(id);

        uiModel.addAttribute("medicalHistory", medicalHistory);

        Gender gender = medicalHistory.getRegister().getPatient().getGender();
        boolean isFemale = gender == Gender.FEMALE;
        uiModel.addAttribute("isFemale", isFemale);

        return MEDICAL_HISTORY_EDIT_PAGE;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String update(@Valid MedicalHistory medicalHistory,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return MEDICAL_HISTORY_EDIT_PAGE;
        }

        MedicalHistory saveMedicalHistory = medicalHistoryService.save(medicalHistory);
        redirectAttributes.addFlashAttribute("message", "Medical History successfully updated");

        return REDIRECT_REGISTER_MEDICAL_HISTORY_PAGE + saveMedicalHistory.getRegister().getId();
    }

    @RequestMapping(value = "cancel/{registerId}", method = RequestMethod.GET)
    public String cancel(@PathVariable Long registerId) {

        return "redirect:/patient/show/" + registerService.findOne(registerId).getPatient().getId();
    }
}
