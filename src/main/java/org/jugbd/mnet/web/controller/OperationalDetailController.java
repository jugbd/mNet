package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.OperationalDetail;
import org.jugbd.mnet.domain.Register;
import org.jugbd.mnet.service.OperationalDetailService;
import org.jugbd.mnet.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bazlur Rahman Rokon
 * @date 12/26/14.
 */
@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("operationaldetail")
public class OperationalDetailController {

    public static final String OPERATIONAL_DETAIL_CREATE_PAGE = "operationaldetail/create";
    public static final String OPERATIONAL_DETAIL_SHOW_PAGE = "operationaldetail/show";
    public static final String OPERATIONAL_DETAIL_EDIT_PAGE = "operationaldetail/edit";
    public static final String REDIRECT_OPERATIONAL_DETAIL_SHOW_PAGE = "redirect:/operationaldetail/show/";
    public static final String REDIRECT_REGISTER_OPERATIONAL_DETAIL_PAGE = "redirect:/register/operationaldetail/";

    @Autowired
    private RegisterService registerService;

    @Autowired
    private OperationalDetailService operationalDetailService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy hh:mm a"), true));
    }

    @RequestMapping(value = "create/{registerId}", method = RequestMethod.GET)
    public String create(@PathVariable Long registerId, OperationalDetail operationalDetail, Model uiModel) {

        Register register = registerService.findOne(registerId);
        operationalDetail.setRegister(register);

        return OPERATIONAL_DETAIL_CREATE_PAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@Valid OperationalDetail operationalDetail,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return OPERATIONAL_DETAIL_CREATE_PAGE;
        }

        OperationalDetail operationalDetailSaved = operationalDetailService.save(operationalDetail);
        redirectAttributes.addFlashAttribute("message", "Operational Detail successfully created");

        return REDIRECT_OPERATIONAL_DETAIL_SHOW_PAGE + operationalDetailSaved.getId();
    }

    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id, Model uiModel) {
        OperationalDetail operationalDetail = operationalDetailService.findOne(id);
        uiModel.addAttribute("operationalDetail", operationalDetail);

        return OPERATIONAL_DETAIL_SHOW_PAGE;
    }


    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model uiModel) {
        OperationalDetail operationalDetail = operationalDetailService.findOne(id);
        uiModel.addAttribute("operationalDetail", operationalDetail);

        return OPERATIONAL_DETAIL_EDIT_PAGE;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String update(@Valid OperationalDetail operationalDetail,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return OPERATIONAL_DETAIL_EDIT_PAGE;
        }

        OperationalDetail operationalDetailSaved = operationalDetailService.save(operationalDetail);
        redirectAttributes.addFlashAttribute("message", "Operational Detail successfully updated");

        return REDIRECT_OPERATIONAL_DETAIL_SHOW_PAGE + operationalDetailSaved.getId();
    }

    @RequestMapping(value = "cancel/{registerId}", method = RequestMethod.GET)
    public String cancel(@PathVariable Long registerId) {

        return REDIRECT_REGISTER_OPERATIONAL_DETAIL_PAGE + registerId;
    }
}
