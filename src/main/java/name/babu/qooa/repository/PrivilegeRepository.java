package name.babu.qooa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import name.babu.qooa.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

  Privilege findByName(String name);

}
