package name.babu.qooa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import name.babu.qooa.repository.QARepository;

/**
 * XXX TODO create javadoc
 */
@Controller
@RequestMapping("questions")
public class QAController {

  private final QARepository qas;

  @Autowired
  public QAController(QARepository qas) {
    this.qas = qas;
  }
  @RequestMapping("{questionId}")
  public String questionDetail(@PathVariable long questionId, Model model) {
    model.addAttribute("question", qas.findOne(questionId));
    return "question";
  }

}
