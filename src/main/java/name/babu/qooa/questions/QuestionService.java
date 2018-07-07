package name.babu.qooa.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import name.babu.qooa.model.DTOUser;
import name.babu.qooa.model.Question;
import name.babu.qooa.repository.QARepository;
import name.babu.qooa.repository.UserRepository;

@Service
public class QuestionService {

  @Autowired
  private QARepository qas;
  @Autowired
  private UserRepository userRepo;

  @Transactional
  public void upvote(String questionId) throws AlreadyVotedException {
    // user already voted? ---------------------
    User userSpring = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    DTOUser user = userRepo.findByUsername(userSpring.getUsername());
    if (user.getVotedUpQuestionIds().contains(questionId)) {
      throw new AlreadyVotedException();
    }
    // write to user --------------------------
    user.addVotedQuestions(questionId);
    user.removeDownVotedQuestion(questionId);
    userRepo.save(user);
    // increment ------------------------------
    Question question = qas.findOne(questionId);
    question.incrementVotes();
    qas.save(question);
  }

  @Transactional
  public void downvote(String questionId) throws AlreadyDownVotedException {
    // user already downvoted for this question? ------
    User userSpring = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    DTOUser user = userRepo.findByUsername(userSpring.getUsername());
    if (user.getVotedDownQuestionIds().contains(questionId)) {
      throw new AlreadyDownVotedException();
    }
    // write to user --------------------------
    user.addDownvotedQuestion(questionId);
    user.removeUpVotedQuestion(questionId);
    userRepo.save(user);
    // decrement -------------------------------
    Question question = qas.findOne(questionId);
    question.decrement();
    qas.save(question);
  }

}
