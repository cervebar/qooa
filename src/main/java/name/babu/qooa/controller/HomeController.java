package name.babu.qooa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import name.babu.qooa.model.Question;
import name.babu.qooa.repository.QARepository;

@Controller
public class HomeController {

  private final QARepository qas;

  @Autowired
  public HomeController(QARepository qas) {
    this.qas = qas;
  }

  @RequestMapping("")
  public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                         @PageableDefault Pageable pageable,
                         Model model) {
    Page<Question> questions = qas.findAll(pageable);
    model.addAttribute("message", name);
    model.addAttribute("questions", questions);
    return "home";
  }

}
