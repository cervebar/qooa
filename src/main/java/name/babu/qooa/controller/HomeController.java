package name.babu.qooa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import name.babu.qooa.language.LanguageService;
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


}
