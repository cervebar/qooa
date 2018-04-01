package name.babu.qooa.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * XXX TODO create javadoc
 */
@Data
@Entity
public class Tag {
  
  @Id
  private String name;
  
  //TODO created by
  
  public Tag() {}
  
  public Tag(String name) {
    this.name = name;
  }

}
