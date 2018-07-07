package name.babu.qooa.qa;

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

import name.babu.qooa.frontend.info.AnswerForm;
import name.babu.qooa.frontend.info.InfoUser;
import name.babu.qooa.language.LanguageService;
import name.babu.qooa.qa.model.Question;
import name.babu.qooa.skin.SkinService;
import name.babu.qooa.user.DTOUser;
import name.babu.qooa.user.repository.UserRepository;

@Controller
@RequestMapping("")
public class QAController {

  private static final Logger LOG = Logger.getLogger(QAController.class);

  private final QuestionRepository qas;
  private final QuestionService questionService;
  private final LanguageService lang;
  private final SkinService skin;
  private final UserRepository userRepo;

  private AnswerService answerService;

  @Autowired
  public QAController(QuestionRepository qas,
                      LanguageService lang,
                      SkinService skin,
                      UserRepository userRepo,
                      QuestionService questionService, AnswerService answerService) {
    this.qas = qas;
    this.lang = lang;
    this.skin = skin;
    this.userRepo = userRepo;
    this.questionService = questionService;
    this.answerService = answerService;
  }


  @RequestMapping(value = { "", "home" })
  public String home(@PageableDefault Pageable pageable, Model model) {
    Page<Question> questions = qas.findAll(pageable);
    model.addAttribute("questions", questions);
    addContextInfo(model);
    return "home";
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
    // TODO hash id nejak rozumneji id a pres service
    byte[] encodedBytes = Base64.getEncoder().encode(question.getTitle().getBytes());
    String id = new String(encodedBytes);
    question.setId(id);
    question.setCreatedOn(new Date());
    qas.save(question);

    // TODO return error if title already exists

    // TODO tags

    return new ModelAndView("redirect:/questions/" + id);
  }

  // one question view -----------------------------------------------------
  @GetMapping("questions/{questionId}")
  public String questionDetail(@PathVariable String questionId, Model model) {
    Question question = qas.findOne(questionId);
    model.addAttribute("question", question);
    model.addAttribute("answers", question.getAnswers());
    model.addAttribute("answerForm", new AnswerForm());
    addContextInfo(model);
    return "question";
  }

  @PostMapping(value = "/questions/{questionId}/answers/{answerId}", params = { "downvote" })
  @PreAuthorize("isAuthenticated()")
  // TODO has role, has reputation ... or some rule if he may downvote
  public String downvoteAnswer(@PathVariable String questionId, @PathVariable String answerId) {
    try {
      answerService.downvote(answerId);
    } catch (AlreadyDownVotedException e) {
      // TODO
      LOG.info("CREATE Error handel for down vote");
      e.printStackTrace();
    }
    return "redirect:/questions/" + questionId;
    // TODO return to # hover on correct answerid #answer1234
  }

  @PostMapping(value = "/questions/{questionId}/answers/{answerId}", params = { "upvote" })
  @PreAuthorize("isAuthenticated()")
  public String upVoteAnswer(@PathVariable String questionId, @PathVariable String answerId) {
    try {
      answerService.upvote(answerId);
    } catch (AlreadyVotedException e) {
      // TODO
      LOG.info("CREATE Error handel for down vote");
      e.printStackTrace();
    }
    return "redirect:/questions/" + questionId;
    // TODO return to # hover on correct answerid #answer1234
  }

  @PostMapping("questions/{questionId}/answers")
  public String addAnswer(@PathVariable String questionId, Model model, @ModelAttribute AnswerForm answerForm) {
    LOG.info("kvak" + answerForm.getContent());
    Question question = questionService.addAnswer(questionId, answerForm.getContent(), getSessionUserName());
    model.addAttribute("question", question);
    model.addAttribute("answers", question.getAnswers());
    model.addAttribute("answerForm", new AnswerForm());
    addContextInfo(model);
    return "question";
  }

  // helping functions -----------------------------------------------------

  private String getSessionUserName() {
    return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
  }

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
