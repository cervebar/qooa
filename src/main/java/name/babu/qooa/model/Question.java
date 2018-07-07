package name.babu.qooa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

/**
 * XXX TODO create javadoc
 */
@Data
@Entity
public class Question {

  private @Id String id;
  private String title;
  private String content;// markdown
  // private String byte[]; other content in protobuf? images
  private long createdOn;
  
  private long voteCount;
  
  //TODO
  private int answers;
  
  @ManyToMany
  private List<Tag> tags;
  
  public Question() {}

  public Question(String id, String title, String content, Long createdOn, long voteCount, int answers, List<Tag> tags) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdOn = createdOn;
    this.voteCount = voteCount;
    this.answers = answers;
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

  // author
  // tags
  // rules
  // updated,was edited
  // timestamp updated
}
