package name.babu.qooa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import name.babu.qooa.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

  Role findByName(String name);
}