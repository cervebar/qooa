package name.babu.qooa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import name.babu.qooa.language.LanguageService;
import name.babu.qooa.model.Question;
import name.babu.qooa.repository.QARepository;
import name.babu.qooa.skin.SkinService;

@Controller
public class HomeController {

  private final QARepository qas;
  private final LanguageService lang;
  private final SkinService skin;

  @Autowired
  public HomeController(QARepository qas, LanguageService lang, SkinService skin) {
    this.qas = qas;
    this.lang = lang;
    this.skin = skin;
  }

  @RequestMapping("")
  public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                         @PageableDefault Pageable pageable,
                         Model model) {
    Page<Question> questions = qas.findAll(pageable);
    model.addAttribute("message", name);
    model.addAttribute("questions", questions);
    model.addAttribute("lang", lang.getContext());
    model.addAttribute("skin", skin.getSkin());
    return "home";
  }

}
