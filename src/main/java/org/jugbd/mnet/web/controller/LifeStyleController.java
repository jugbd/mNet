package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.LifeStyle;
import org.jugbd.mnet.domain.Register;
import org.jugbd.mnet.service.LifeStyleService;
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
@RequestMapping("lifestyle")
public class LifeStyleController {

    public static final String LIFESTYLE_CREATE_PAGE = "lifestyle/create";
    public static final String REDIRECT_REGISTER_LIFESTYLE_PAGE = "redirect:/register/lifestyle/";
    public static final String LIFESTYLE_EDIT_PAGE = "lifestyle/edit";

    @Autowired
    private LifeStyleService lifeStyleService;

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "create/{registerId}", method = RequestMethod.GET)
    public String create(@PathVariable Long registerId, LifeStyle lifeStyle) {
        Register register = registerService.findOne(registerId);
        lifeStyle.setRegister(register);

        return LIFESTYLE_CREATE_PAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@Valid LifeStyle lifeStyle,
                       BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return LIFESTYLE_CREATE_PAGE;
        }

        LifeStyle lifeStyleSaved = lifeStyleService.save(lifeStyle);
        redirectAttributes.addFlashAttribute("message", "Life Style successfully created");

        return REDIRECT_REGISTER_LIFESTYLE_PAGE + lifeStyleSaved.getRegister().getId();
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model uiModel) {
        LifeStyle lifeStyle = lifeStyleService.findOne(id);

        uiModel.addAttribute("lifeStyle", lifeStyle);

        return LIFESTYLE_EDIT_PAGE;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String update(@Valid LifeStyle lifeStyle,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {

            return LIFESTYLE_EDIT_PAGE;
        }

        LifeStyle savedLifeStyle = lifeStyleService.save(lifeStyle);
        redirectAttributes.addFlashAttribute("message", "Life Style successfully updated");

        return REDIRECT_REGISTER_LIFESTYLE_PAGE + savedLifeStyle.getRegister().getId();
    }

    @RequestMapping(value = "cancel/{registerId}", method = RequestMethod.GET)
    public String cancel(@PathVariable Long registerId) {

        return REDIRECT_REGISTER_LIFESTYLE_PAGE + registerId;
    }
}
