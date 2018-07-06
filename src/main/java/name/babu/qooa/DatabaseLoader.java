/*
 * Copyright 2015 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package name.babu.qooa;

import static java.util.Arrays.asList;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import name.babu.qooa.model.Privilege;
import name.babu.qooa.model.Question;
import name.babu.qooa.model.Role;
import name.babu.qooa.model.Tag;
import name.babu.qooa.model.User;
import name.babu.qooa.repository.PrivilegeRepository;
import name.babu.qooa.repository.QARepository;
import name.babu.qooa.repository.RoleRepository;
import name.babu.qooa.repository.TagRepository;
import name.babu.qooa.service.UserService;

/**
 * TODO : move test login into profile
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

  private final QARepository qas;
  private final TagRepository tagr;
  private final UserService userrep;
  private final RoleRepository rolrep;
  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Autowired
  public DatabaseLoader(QARepository qas, TagRepository tagr, UserService userrep, RoleRepository rolrep) {
    this.qas = qas;
    this.tagr = tagr;
    this.userrep = userrep;
    this.rolrep = rolrep;
  }

  @Override
  public void run(String... strings) throws Exception {

    // example quars

    List<Tag> tags1 = new ArrayList<>();
    List<Tag> tags2 = new ArrayList<>();

    Tag t1 = tagr.save(new Tag("aaa"));
    Tag t2 = tagr.save(new Tag("bb"));
    Tag t3 = tagr.save(new Tag("c"));

    tags1.add(t1);
    tags1.add(t2);
    tags1.add(t3);
    tags2.add(t1);

    this.qas.save(new Question("headline1", "headline", "*some markdown content* heh",
        Instant.now().toEpochMilli(), 12, 3, tags1));
    this.qas.save(new Question("headline2", "headline2 a jeste k tomu tohle max XYZ znaku",
        "*some markdown content* heh", Instant.now().toEpochMilli(), 2, 2, tags2));

    // autorization and authentication -------------------------------

    // priviledges
    Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
    Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

    Set<Privilege> adminPrivileges = new HashSet<>(asList(readPrivilege, writePrivilege));
    createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
    createRoleIfNotFound("ROLE_USER", new HashSet<>(asList(readPrivilege)));

    Role adminRole = rolrep.findByName("ROLE_ADMIN");
    Set<Role> set = new HashSet<>(asList(adminRole));
    User admin = new User();
    admin.setPassword("admin");
    admin.setUsername("admin");
    admin.setFirstName("Babu");
    admin.setEmail("babu@cfhero.org");
    admin.setEnabled(true);
    admin.setRoles(set);
    this.userrep.save(admin);

    User user = new User();
    user.setPassword("user");
    user.setUsername("user");
    user.setFirstName("Franta");
    user.setLastName("Uzivatel");
    user.setEmail("bezny.franta@uzivatel.cz");
    user.setEnabled(true);
    user.setRoles(new HashSet<>(asList(rolrep.findByName("ROLE_USER"))));
    this.userrep.save(user);

    SecurityContextHolder.clearContext();
  }

  @Transactional
  private Privilege createPrivilegeIfNotFound(String name) {
    Privilege privilege = privilegeRepository.findByName(name);
    if (privilege == null) {
      privilege = new Privilege();
      privilege.setName(name);
      privilegeRepository.save(privilege);
    }
    return privilege;
  }

  @Transactional
  private Role createRoleIfNotFound(String name, Set<Privilege> privileges) {
    Role role = rolrep.findByName(name);
    if (role == null) {
      role = new Role();
      role.setName(name);
      role.setPrivileges(privileges);
      rolrep.save(role);
    }
    return role;
  }
}
