package name.babu.qooa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import name.babu.qooa.language.LanguageService;
import name.babu.qooa.repository.QARepository;
import name.babu.qooa.skin.SkinService;

/**
 * XXX TODO create javadoc
 */
@Controller
@RequestMapping("questions")
public class QAController {

  private final QARepository qas;
  private final LanguageService lang;
  private final SkinService skin;

  @Autowired
  public QAController(QARepository qas, LanguageService lang, SkinService skin) {
    this.qas = qas;
    this.lang = lang;
    this.skin = skin;
  }

  @RequestMapping("{questionId}")
  public String questionDetail(@PathVariable long questionId, Model model) {
    model.addAttribute("question", qas.findOne(questionId));
    addContextInfo(model);
    return "question";
  }

  @RequestMapping("ask")
  public String ask(@PathVariable long questionId, Model model) {
    addContextInfo(model);
    return "question-ask";
  }

  private void addContextInfo(Model model) {
    model.addAttribute("lang", lang.getContext());
    model.addAttribute("skin", skin.getSkin());
  }

}
