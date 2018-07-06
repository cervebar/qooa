package name.babu.qooa.controller;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import name.babu.qooa.language.LanguageService;
import name.babu.qooa.model.Question;
import name.babu.qooa.model.forms.Upvote;
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

  @GetMapping("questions/{questionId}")
  public String questionDetail(@PathVariable String questionId, Model model) {
    model.addAttribute("question", qas.findOne(questionId));
    addContextInfo(model);
    model.addAttribute("upvote", new Upvote());
    return "question";
  }

  @GetMapping("questions")
  public String questions(@PageableDefault Pageable pageable,
                          Model model) {
    return home(pageable, model);
  }

  @RequestMapping("")
  public String home(@PageableDefault Pageable pageable, Model model) {
    Page<Question> questions = qas.findAll(pageable);
    model.addAttribute("questions", questions);
    addContextInfo(model);
    return "home";
  }

  @GetMapping("ask")
  public String ask(Model model) {
    addContextInfo(model);
    model.addAttribute("question", new Question());
    return "ask-question";
  }

  @PostMapping("ask")
  @PreAuthorize("hasAuthority('WRITE_PRIVILEDGE')")
  public ModelAndView createQuestion(@ModelAttribute Question question) {
    // TODO hash id nejak rozumneji id
    byte[] encodedBytes = Base64.getEncoder().encode(question.getTitle().getBytes());
    String id = new String(encodedBytes);
    question.setId(id);
    question.setCreatedOn(new Date());
    qas.save(question);

    // TODO return error if title already exists

    // TODO tags

    return new ModelAndView("redirect:/questions/" + id);
  }

  // TODO annotace, session?
  private void addContextInfo(Model model) {
    model.addAttribute("lang", lang.getContext());
    model.addAttribute("skin", skin.getSkin());
    // model.addAttribute("user", skin.getSkin());// user.mini-reputation
  }

}
