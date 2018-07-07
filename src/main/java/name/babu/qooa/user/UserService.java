package name.babu.qooa.user;

public interface UserService {

  void save(DTOUser user);

  DTOUser findByUsername(String username);
}