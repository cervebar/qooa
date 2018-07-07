package name.babu.qooa.frontend.info;

import lombok.Value;
import name.babu.qooa.user.DTOUser;

@Value
public class InfoUser {
  private String username;

  public InfoUser(DTOUser user) {
    this.username = user.getUsername();
  }
  
}
