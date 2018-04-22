/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package name.babu.qooa;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import name.babu.qooa.model.Question;
import name.babu.qooa.model.Role;
import name.babu.qooa.model.Tag;
import name.babu.qooa.model.User;
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

    this.qas
        .save(new Question("headline1", "headline", "*some markdown content* heh", Instant.now().toEpochMilli(),
            12, 3, tags1));
    this.qas
        .save(new Question("headline2", "headline2 a jeste k tomu tohle max XYZ znaku",
            "*some markdown content* heh",
            Instant.now().toEpochMilli(), 2, 2, tags2));


    Role role = new Role();
    role.setName("user");
    Set<User> users = new HashSet<>();
    role.setUsers(users);
    this.rolrep.saveAndFlush(role);

    Role saved = rolrep.findByName("user");

    Set<Role> set = new HashSet<>(Arrays.asList(saved));
    User u = new User();
    u.setPassword("b");
    u.setUsername("a");
    u.setRoles(set);
    this.userrep.save(u);

		SecurityContextHolder.clearContext();
	}
}
