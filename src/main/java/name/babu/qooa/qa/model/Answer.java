package name.babu.qooa.qa.model;

import java.time.Instant;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Answer {

  @Id
  private String id;
  private String content;// html
  private long createdOn;
  private long voteCount;

  // TODO created by User, edited by

  public Answer(Question question, String content) {
    this.id = question.getId() + question.getAnswerSize() + new Random().nextLong();
    this.createdOn = Instant.now().toEpochMilli();
    this.content = content;
    this.voteCount = 0;
  }

  public Answer() {
  }

  public void downvote() {
    this.voteCount--;
  }

  public void upvote() {
    this.voteCount++;
  }

}
