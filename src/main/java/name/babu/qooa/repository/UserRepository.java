package name.babu.qooa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import name.babu.qooa.model.User;

/**
 * XXX TODO create javadoc
 */
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  @Override
  User save(User tag);

  User findByEmail(String email);

}
