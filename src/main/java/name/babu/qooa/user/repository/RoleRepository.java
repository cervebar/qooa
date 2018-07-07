package name.babu.qooa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import name.babu.qooa.user.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

  Role findByName(String name);
}