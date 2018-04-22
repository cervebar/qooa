package name.babu.qooa.login;

public interface SecurityService {

  String findLoggedInUsername();
  void autologin(String username, String password);
}