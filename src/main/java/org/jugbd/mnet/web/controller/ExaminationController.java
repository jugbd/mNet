package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.Examination;
import org.jugbd.mnet.domain.enums.RegistrationType;
import org.jugbd.mnet.service.ExaminationService;
import org.jugbd.mnet.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Bazlur Rahman Rokon
 * @date 11/29/2014.
 */
@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("examination")
public class ExaminationController {

    public static final String EXAMINATION_CREATE_PAGE = "examination/create";
    public static final String EXAMINATION_EDIT_PAGE = "examination/edit";
    public static final String REDIRECT_REGISTER_EXAMINATION_PAGE = "redirect:/register/examination/";

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "create/{registerId}", method = RequestMethod.GET)
    public String create(@PathVariable Long registerId,
                         @RequestParam(required = true) RegistrationType registrationType,
                         Examination examination,
                         Model uiModel) {

        uiModel.addAttribute("registrationType", registrationType);
        registerService.findRegisterEither(registerId, registrationType)
                .map(examination::setRegister, examination::setOutdoorRegister);

        return EXAMINATION_CREATE_PAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@RequestParam RegistrationType registrationType,
                       @Valid Examination examination,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return EXAMINATION_CREATE_PAGE;
        }

        Examination examinationFromDb = examinationService.save(examination, registrationType);
        redirectAttributes.addFlashAttribute("message", "Diagnosis successfully created!");

        return getRedirectUrl(registrationType, examinationFromDb);
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id,
                       @RequestParam RegistrationType registrationType,
                       Model uiModel) {

        Examination examination = examinationService.findOne(id);
        uiModel.addAttribute("examination", examination);
        uiModel.addAttribute("registrationType", registrationType);

        return EXAMINATION_EDIT_PAGE;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String update(@RequestParam RegistrationType registrationType,
                         @Valid Examination examination,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return EXAMINATION_EDIT_PAGE;
        }

        Examination examinationFromDb = examinationService.save(examination, registrationType);
        redirectAttributes.addFlashAttribute("message", "Examination successfully updated");

        return getRedirectUrl(registrationType, examinationFromDb);
    }

    @RequestMapping(value = "cancel/{registerId}", method = RequestMethod.GET)
    public String cancel(@PathVariable Long registerId, @RequestParam RegistrationType registrationType) {

        return REDIRECT_REGISTER_EXAMINATION_PAGE + registerService.findOne(registerId).getId() + "?registrationType=" + registrationType;
    }

    private String getRedirectUrl(RegistrationType registrationType, Examination examination) {
        String appender = "?registrationType=" + registrationType;

        return (registrationType == RegistrationType.OUTDOOR)
                ? (String.format("%s%d%s", REDIRECT_REGISTER_EXAMINATION_PAGE, examination.getOutdoorRegister().getId(), appender))
                : (String.format("%s%d%s", REDIRECT_REGISTER_EXAMINATION_PAGE, examination.getRegister().getId(), appender));
    }
}
