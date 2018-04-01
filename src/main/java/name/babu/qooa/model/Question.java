package name.babu.qooa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

/**
 * XXX TODO create javadoc
 */
@Data
@Entity
public class Question {

  private @Id @GeneratedValue Long id;
  private String title;
  private String content;// markdown
  // private String byte[]; other content in protobuf? images
  private long createdOn;
  
  // TODO 
  private int voteCount;
  private int answers;
  
  @ManyToMany
  private List<Tag> tags;
  
  private Question() {}

  public Question(String title, String content, Long createdOn, int voteCount, int answers, List<Tag> tags) {
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

  // author
  // upvotes
  // tags
  // rules
  // updated,was edited
  // timestamp updated
}
