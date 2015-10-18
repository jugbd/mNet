package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.ChiefComplaint;
import org.jugbd.mnet.domain.enums.RegistrationType;
import org.jugbd.mnet.service.ChiefComplaintService;
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
@RequestMapping("chiefcomplaints")
public class ChiefComplaintsController {

    public static final String REDIRECT_REGISTER_CHIEF_COMPLAINTS_PAGE = "redirect:/register/chiefcomplaints/";
    public static final String CHIEF_COMPLAINTS_CREATE_SHOW = "chiefcomplaints/create";
    public static final String CHIEF_COMPLAINTS_EDIT_PAGE = "chiefcomplaints/edit";

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ChiefComplaintService chiefComplaintService;

    @RequestMapping(value = "create/{registerId}", method = RequestMethod.GET)
    public String create(@PathVariable Long registerId,
                         @RequestParam(required = true) RegistrationType registrationType,
                         ChiefComplaint chiefComplaint,
                         Model uiModel) {

        uiModel.addAttribute("registrationType", registrationType);
        registerService.findRegisterEither(registerId, registrationType)
                .map(chiefComplaint::setRegister, chiefComplaint::setOutdoorRegister);

        return CHIEF_COMPLAINTS_CREATE_SHOW;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@RequestParam(required = true) RegistrationType registrationType,
                       @Valid ChiefComplaint chiefComplaint,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return CHIEF_COMPLAINTS_CREATE_SHOW;
        }

        ChiefComplaint savedChiefComplaint = chiefComplaintService.save(chiefComplaint, registrationType);
        redirectAttributes.addFlashAttribute("message", "Chief Complaint successfully created");

        return getRedirectUrl(registrationType, savedChiefComplaint);
    }


    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id,
                       @RequestParam RegistrationType registrationType,
                       Model uiModel) {
        ChiefComplaint chiefComplaint = chiefComplaintService.findOne(id);

        uiModel.addAttribute("chiefComplaint", chiefComplaint);
        uiModel.addAttribute("registrationType", registrationType);

        return CHIEF_COMPLAINTS_EDIT_PAGE;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String update(@RequestParam RegistrationType registrationType,
                         @Valid ChiefComplaint chiefComplaint,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {

            return CHIEF_COMPLAINTS_EDIT_PAGE;
        }

        ChiefComplaint savedChiefComplaint = chiefComplaintService.save(chiefComplaint, registrationType);
        redirectAttributes.addFlashAttribute("message", "Chief Complaints successfully updated");

        return getRedirectUrl(registrationType, savedChiefComplaint);
    }

    @RequestMapping(value = "back/{registerId}", method = RequestMethod.GET)
    public String back(@PathVariable Long registerId, @RequestParam RegistrationType registrationType) {

        return REDIRECT_REGISTER_CHIEF_COMPLAINTS_PAGE + registerId + "?registrationType=" + registrationType;
    }

    private String getRedirectUrl(RegistrationType registrationType, ChiefComplaint chiefComplaint) {
        String appender = "?registrationType=" + registrationType;

        return (registrationType == RegistrationType.OUTDOOR)
                ? (String.format("%s%d%s", REDIRECT_REGISTER_CHIEF_COMPLAINTS_PAGE, chiefComplaint.getOutdoorRegister().getId(), appender))
                : (String.format("%s%d%s", REDIRECT_REGISTER_CHIEF_COMPLAINTS_PAGE, chiefComplaint.getRegister().getId(), appender));
    }
}
