package name.babu.qooa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import name.babu.qooa.language.LanguageService;
import name.babu.qooa.model.Question;
import name.babu.qooa.repository.QARepository;
import name.babu.qooa.skin.SkinService;

/**
 * XXX TODO create javadoc
 */
@Controller
@RequestMapping("")
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

  @RequestMapping("questions/{questionId}")
  public String questionDetail(@PathVariable long questionId, Model model) {
    model.addAttribute("question", qas.findOne(questionId));
    addContextInfo(model);
    return "question";
  }

  @RequestMapping("")
  public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                         @PageableDefault Pageable pageable, Model model) {
    Page<Question> questions = qas.findAll(pageable);
    model.addAttribute("message", name);
    model.addAttribute("questions", questions);
    addContextInfo(model);
    return "home";
  }

  @RequestMapping("ask")
  public String ask(Model model) {
    addContextInfo(model);
    return "ask-question";
  }

  private void addContextInfo(Model model) {
    model.addAttribute("lang", lang.getContext());
    model.addAttribute("skin", skin.getSkin());
  }

}
