package name.babu.qooa.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import name.babu.qooa.model.User;
import name.babu.qooa.service.UserService;

@Controller
public class LoginRegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private RegistrationValidator regValidator;

  @RequestMapping(value = "/registration", method = RequestMethod.GET)
  public String registration(Model model) {
    model.addAttribute("userForm", new User());
    return "registration";
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
    regValidator.validate(userForm, bindingResult);
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", bindingResult.getAllErrors());
      model.addAttribute("userForm", userForm);
      return "registration";
    }
    userService.save(userForm);
    securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
    return "redirect:/hello";
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

  @RequestMapping(value = { "/", "/hello" }, method = RequestMethod.GET)
  public String welcome(Model model) {
    return "hello";
  }
}