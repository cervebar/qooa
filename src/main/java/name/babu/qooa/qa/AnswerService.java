package name.babu.qooa.qa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import name.babu.qooa.qa.model.Answer;
import name.babu.qooa.user.DTOUser;
import name.babu.qooa.user.repository.UserRepository;

@Service
public class AnswerService {

  @Autowired
  private UserRepository userRepo;
  @Autowired
  AnswerRepository answRepo;

  @Transactional
  public void downvote(String answerId) throws AlreadyDownVotedException {
    // user already downvoted for this answer? ------
    User userSpring = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    DTOUser user = userRepo.findByUsername(userSpring.getUsername());
    if (user.getDownVotedAnswerIds().contains(answerId)) {
      throw new AlreadyDownVotedException();
    }
    // write to user --------------------------
    user.addDowVotedAnswer(answerId);
    user.removeUpVotedAnswer(answerId);
    userRepo.save(user);
    // decrement -------------------------------
    Answer a = answRepo.findOne(answerId);
    a.downvote();
    answRepo.save(a);
  }

  @Transactional
  public void upvote(String answerId) throws AlreadyVotedException {
    // user already upvoted for this answer? ------
    User userSpring = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    DTOUser user = userRepo.findByUsername(userSpring.getUsername());
    if (user.getUpVotedAnswerIds().contains(answerId)) {
      throw new AlreadyVotedException();
    }
    // write to user --------------------------
    user.addUpVotedAnswer(answerId);
    user.removeDownVotedAnswer(answerId);
    userRepo.save(user);
    // increment -------------------------------
    Answer a = answRepo.findOne(answerId);
    a.upvote();
    answRepo.save(a);
  }

}
