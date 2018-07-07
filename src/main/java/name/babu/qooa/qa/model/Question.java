package name.babu.qooa.qa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;

/**
 * XXX TODO create javadoc
 */
@Data
@Entity
public class Question {

  private @Id String id;
  private String title;
  private String content;// html
  // private String byte[]; other content in protobuf? images
  private long createdOn;
  
  //TODO created by User
  
  private long voteCount;
  
  //TODO vyznam cascadetype, LAZY
  @OneToMany(cascade=CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
  private List<Answer> answers = new ArrayList<>();
  
  //TODO embedable - there is no need to store anwswer to a differnet - when I want answers I want them right - not for MVP
  
  // author
  // tags
  // rules
  // updated,was edited
  // timestamp updated
  
  
  @ManyToMany
  private List<Tag> tags;
  
  public Question() {}

  public Question(String id, String title, String content, Long createdOn, long voteCount,List<Tag> tags) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdOn = createdOn;
    this.voteCount = voteCount;
    this.tags = tags;
  }

  public Date getCreatedOn() {
    return new Date(createdOn);
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn.getTime();
  }

  public void incrementVotes() {
    this.voteCount++;
  }

  public void decrement() {
    this.voteCount--;
  }

  public int getAnswerSize() {
    return answers.size();
  }

  public void addAnswer(Answer a1) {
    this.answers.add(a1);
  }

}
