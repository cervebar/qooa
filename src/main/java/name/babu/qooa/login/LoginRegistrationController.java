package name.babu.qooa.login;

import static java.util.Arrays.asList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import name.babu.qooa.model.DTOUser;
import name.babu.qooa.repository.RoleRepository;
import name.babu.qooa.service.UserService;

@Controller
public class LoginRegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private RegistrationValidator regValidator;

  @Autowired
  private RoleRepository roleRepository;

  @RequestMapping(value = "/registration", method = RequestMethod.GET)
  public String registration(Model model) {
    model.addAttribute("userForm", new DTOUser());
    return "registration";
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public String registration(@ModelAttribute("userForm") DTOUser userForm, BindingResult bindingResult, Model model) {
    regValidator.validate(userForm, bindingResult);
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", bindingResult.getAllErrors());
      model.addAttribute("userForm", userForm);
      return "registration";
    }
    userForm.setRoles(asList(roleRepository.findByName("ROLE_USER")));
    userForm.setEnabled(true);
    userService.save(userForm);
    securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
    return "redirect:/home";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model, String error, String logout) {
    if (error != null) {
      model.addAttribute("error", "Your username and password is invalid.");
    }
    if (logout != null) {
      model.addAttribute("message", "You have been logged out successfully.");
    }
    return "login";
  }

}