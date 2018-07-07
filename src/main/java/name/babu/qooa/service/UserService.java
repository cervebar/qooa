package name.babu.qooa.service;

import name.babu.qooa.model.DTOUser;

public interface UserService {

  void save(DTOUser user);

  DTOUser findByUsername(String username);
}