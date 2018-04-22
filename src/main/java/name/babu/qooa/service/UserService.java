package name.babu.qooa.service;

import name.babu.qooa.model.User;

public interface UserService {

  void save(User user);

  User findByUsername(String username);
}