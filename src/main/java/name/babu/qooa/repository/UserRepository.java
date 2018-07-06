package name.babu.qooa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import name.babu.qooa.model.DTOUser;

/**
 * XXX TODO create javadoc
 */
public interface UserRepository extends JpaRepository<DTOUser, Long> {

  DTOUser findByUsername(String username);

  @Override
  DTOUser save(DTOUser tag);

  DTOUser findByEmail(String email);

}
