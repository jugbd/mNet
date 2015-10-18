package org.jugbd.mnet.web.controller;

import org.jugbd.mnet.domain.User;
import org.jugbd.mnet.domain.enums.Role;
import org.jugbd.mnet.service.UserService;
import org.jugbd.mnet.utils.PageWrapper;
import org.jugbd.mnet.web.editor.AuthorityEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by Bazlur Rahman Rokon on 7/15/14.
 */

@Controller
@RequestMapping("/user")
@Secured("ROLE_ADMIN")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public static final String USER_CREATE_PAGE = "user/create";
    public static final String REDIRECT_USER_SHOW_PAGE = "redirect:/user/show/";
    public static final String USER_INDEX_PAGE = "user/index";
    public static final String USER_SHOW_PAGE = "user/show";
    public static final String USER_EDIT_PAGE = "user/edit";
    public static final String REDIRECT_USER_INDEX_PAGE = "redirect:/user";

    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Role.class, new AuthorityEditor());
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(User user) {

        return USER_CREATE_PAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@Valid User user,
                       BindingResult result,
                       RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {

            return USER_CREATE_PAGE;
        }

        User userFound = userService.findByUserName(user.getUsername());
        if (userFound != null) {
            result.rejectValue("username", "error.user.username.already.available", "Its look like someone already has that username. Try another");
            return USER_CREATE_PAGE;
        }

        userService.save(user);
        redirectAttrs.addFlashAttribute("message", "Successfully user created");

        return REDIRECT_USER_SHOW_PAGE + user.getId().toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model uiModel, Pageable pageable) {

        Page<User> users = userService.findAll(pageable);
        PageWrapper<User> page = new PageWrapper<>(users, "/user");
        uiModel.addAttribute("page", page);

        return USER_INDEX_PAGE;
    }

    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        log.debug("show() id ={}", id);

        User user = userService.findById(id);
        uiModel.addAttribute("user", user);

        return USER_SHOW_PAGE;
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model uiModel) {
        log.debug("edit() id ={}", id);

        User user = userService.findById(id);
        user.setPassword(null);
        uiModel.addAttribute("user", user);

        return USER_EDIT_PAGE;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("user") User user,
                         BindingResult result,
                         RedirectAttributes redirectAttrs) {
        log.debug("update() user ={}", user);

        if (result.hasErrors()) {
            return USER_EDIT_PAGE;
        }

        userService.save(user);
        redirectAttrs.addFlashAttribute("message", "Successfully user updated");

        return REDIRECT_USER_SHOW_PAGE + user.getId().toString();
    }

    @RequestMapping(value = "cancel", method = RequestMethod.GET)
    public String cancel() {
        log.debug("cancel()");

        return REDIRECT_USER_INDEX_PAGE;
    }
}
