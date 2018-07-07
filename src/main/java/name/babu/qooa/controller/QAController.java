package name.babu.qooa.controller;

import java.util.Base64;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import name.babu.qooa.frontend.info.InfoUser;
import name.babu.qooa.language.LanguageService;
import name.babu.qooa.model.DTOUser;
import name.babu.qooa.model.Question;
import name.babu.qooa.model.forms.VoteAction;
import name.babu.qooa.questions.AlreadyDownVotedException;
import name.babu.qooa.questions.AlreadyVotedException;
import name.babu.qooa.questions.QuestionService;
import name.babu.qooa.repository.QARepository;
import name.babu.qooa.repository.UserRepository;
import name.babu.qooa.skin.SkinService;

@Controller
@RequestMapping("")
public class QAController {

  private static final Logger LOG = Logger.getLogger(QAController.class);

  private final QARepository qas;
  private final QuestionService questionService;
  private final LanguageService lang;
  private final SkinService skin;
  private final UserRepository userRepo;

  @Autowired
  public QAController(QARepository qas,
                      LanguageService lang,
                      SkinService skin,
                      UserRepository userRepo,
                      QuestionService questionService) {
    this.qas = qas;
    this.lang = lang;
    this.skin = skin;
    this.userRepo = userRepo;
    this.questionService = questionService;
  }


  @RequestMapping(value = { "", "home" })
  public String home(@PageableDefault Pageable pageable, Model model) {
    Page<Question> questions = qas.findAll(pageable);
    model.addAttribute("questions", questions);
    addContextInfo(model);
    return "home";
  }

  @GetMapping("questions/{questionId}")
  public String questionDetail(@PathVariable String questionId, Model model) {
    model.addAttribute("question", qas.findOne(questionId));
    addContextInfo(model);
    model.addAttribute("upvote", new VoteAction());
    return "question";
  }

  @PostMapping(value = "questions/{questionId}/votes", params = { "upvote" })
  @PreAuthorize("isAuthenticated()")
  public String upvote(@PathVariable String questionId) {
    try {
      questionService.upvote(questionId);
    } catch (AlreadyVotedException e) {
      // TODO
      LOG.info("CREATE Error handel for up vote");
      e.printStackTrace();
    }
    return "redirect:/";
  }

  @PostMapping(value = "questions/{questionId}/votes", params = { "downvote" })
  @PreAuthorize("isAuthenticated()")
  public String downvote(@PathVariable String questionId) {
    try {
      questionService.downvote(questionId);
    } catch (AlreadyDownVotedException e) {
      // TODO
      LOG.info("CREATE Error handel for down vote");
      e.printStackTrace();
    }
    return "redirect:/";
  }

  @GetMapping("ask")
  public String ask(Model model) {
    addContextInfo(model);
    model.addAttribute("question", new Question());
    return "ask-question";
  }

  @PostMapping("ask")
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

  // helping functions -----------------------------------------------------

  private void addContextInfo(Model model) {
    // context --------------------------------------
    model.addAttribute("lang", lang.getContext());
    model.addAttribute("skin", skin.getSkin());

    // authentication -------------------------------
    if(isAuthenticated()) {
      model.addAttribute("authenticated", true);
      User userSpring = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      DTOUser user = userRepo.findByUsername(userSpring.getUsername());
      InfoUser infoUser = new InfoUser(user);
      model.addAttribute("user", infoUser);
    }
    // froms ---------------------------------------
    model.addAttribute("vote", new VoteAction());
    // model.addAttribute("user", skin.getSkin());// user.mini-reputation
  }

  private boolean isAuthenticated() {
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      return false;
    };
    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
    // when Anonymous Authentication is enabled
        !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      return true;
    }
    return false;
  }

}
