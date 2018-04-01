package name.babu.qooa.model.forms;

import lombok.Data;

/**
 * Used in question.html detail view, for up voting and down voting answers/question
 */
@Data
public class Upvote {

  private String type;
  private String id;
  private boolean isUp;

}
