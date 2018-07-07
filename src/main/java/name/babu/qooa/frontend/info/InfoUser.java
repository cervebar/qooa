package name.babu.qooa.frontend.info;

import lombok.Value;
import name.babu.qooa.model.DTOUser;

@Value
public class InfoUser {
  private String username;

  public InfoUser(DTOUser user) {
    this.username = user.getUsername();
  }
  
}
