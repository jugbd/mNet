package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.ComplicationManagement;
import org.jugbd.mnet.domain.Register;
import org.jugbd.mnet.service.ComplicationManagementService;
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
 * @date 12/26/14.
 */
@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("complicationmanagement")
public class ComplicationManagementController {

    public static final String COMPLICATION_MANAGEMENT_CREATE_PAGE = "complicationmanagement/create";
    public static final String COMPLICATION_MANAGEMENT_EDIT_PAGE = "complicationmanagement/edit";
    public static final String REDIRECT_REGISTER_COMPLICATION_MANAGEMENT_PAGE = "redirect:/register/complicationmanagement/";

    @Autowired
    private ComplicationManagementService complicationManagementService;

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "create/{registerId}", method = RequestMethod.GET)
    public String create(@PathVariable Long registerId, ComplicationManagement complicationManagement) {
        Register register = registerService.findOne(registerId);
        complicationManagement.setRegister(register);

        return COMPLICATION_MANAGEMENT_CREATE_PAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@Valid ComplicationManagement complicationManagement,
                       BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return COMPLICATION_MANAGEMENT_CREATE_PAGE;
        }

        ComplicationManagement complicationManagementSaved = complicationManagementService.save(complicationManagement);
        redirectAttributes.addFlashAttribute("message", "Complication Management successfully created");

        return REDIRECT_REGISTER_COMPLICATION_MANAGEMENT_PAGE + complicationManagementSaved.getRegister().getId();
    }


    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model uiModel) {
        ComplicationManagement complicationManagement = complicationManagementService.findOne(id);
        uiModel.addAttribute("complicationManagement", complicationManagement);

        return COMPLICATION_MANAGEMENT_EDIT_PAGE;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String update(@Valid ComplicationManagement complicationManagement,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {

            return COMPLICATION_MANAGEMENT_EDIT_PAGE;
        }

        ComplicationManagement complicationManagementSaved = complicationManagementService.save(complicationManagement);
        redirectAttributes.addFlashAttribute("message", "Complication Management successfully updated");
        
        return REDIRECT_REGISTER_COMPLICATION_MANAGEMENT_PAGE + complicationManagementSaved.getRegister().getId();
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable Long id) {
        ComplicationManagement complicationManagement = complicationManagementService.findOne(id);
        complicationManagementService.delete(complicationManagement);

        return REDIRECT_REGISTER_COMPLICATION_MANAGEMENT_PAGE + complicationManagement.getRegister().getId();
    }

    @RequestMapping(value = "cancel/{registerId}", method = RequestMethod.GET)
    public String cancel(@PathVariable Long registerId) {

        return REDIRECT_REGISTER_COMPLICATION_MANAGEMENT_PAGE + registerId;
    }

}
