package name.babu.qooa.tmp;

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

import name.babu.qooa.qa.AnswerRepository;
import name.babu.qooa.qa.QuestionRepository;
import name.babu.qooa.qa.model.Answer;
import name.babu.qooa.qa.model.Question;
import name.babu.qooa.qa.model.Tag;
import name.babu.qooa.user.DTOUser;
import name.babu.qooa.user.Privilege;
import name.babu.qooa.user.Role;
import name.babu.qooa.user.UserService;
import name.babu.qooa.user.repository.PrivilegeRepository;
import name.babu.qooa.user.repository.RoleRepository;
import name.babu.qooa.user.repository.TagRepository;

/**
 * TODO : move test login into profile
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

  private final QuestionRepository qas;
  private final TagRepository tagr;
  private final UserService userrep;
  private final RoleRepository rolrep;
  @Autowired
  private PrivilegeRepository privilegeRepository;
  @Autowired
  private AnswerRepository answRepo;

  @Autowired
  public DatabaseLoader(QuestionRepository qas, TagRepository tagr, UserService userrep, RoleRepository rolrep) {
    this.qas = qas;
    this.tagr = tagr;
    this.userrep = userrep;
    this.rolrep = rolrep;
  }

  @Override
  public void run(String... strings) throws Exception {

    List<Tag> tags1 = new ArrayList<>();
    List<Tag> tags2 = new ArrayList<>();

    Tag t1 = tagr.save(new Tag("aaa"));
    Tag t2 = tagr.save(new Tag("bb"));
    Tag t3 = tagr.save(new Tag("c"));

    tags1.add(t1);
    tags1.add(t2);
    tags1.add(t3);
    tags2.add(t1);

    Question q1 = new Question("headline1", "headline", "<b>some html content</b><div>kvak ,ble, kvak</div> heh",
        Instant.now().toEpochMilli(), 12, tags1);
    Question q2 = new Question("headline2", "headline2 a jeste k tomu tohle max XYZ znaku",
        "<b>some html content</b><p>paragraph</p> heh", Instant.now().toEpochMilli(), 2, tags2);
    this.qas.save(q1);
    this.qas.save(q2);

    Question q11 = qas.findOne("headline1");

    Answer a1 = new Answer(q11, "This is my answer. <div>some content in DIV</div>");
    Answer a2 = new Answer(q11, "This is my answer2. <div>some content in DIV</div>");
    q11.addAnswer(a1);
    q11.addAnswer(a2);
    this.qas.save(q11);

    // autorization and authentication -------------------------------

    // priviledges
    Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
    Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

    Set<Privilege> adminPrivileges = new HashSet<>(asList(readPrivilege, writePrivilege));
    createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
    createRoleIfNotFound("ROLE_USER", new HashSet<>(asList(readPrivilege)));

    Role adminRole = rolrep.findByName("ROLE_ADMIN");
    Set<Role> set = new HashSet<>(asList(adminRole));
    DTOUser admin = new DTOUser();
    admin.setPassword("admin");
    admin.setUsername("admin");
    admin.setFirstName("Babu");
    admin.setEmail("babu@cfhero.org");
    admin.setEnabled(true);
    admin.setRoles(set);
    this.userrep.save(admin);

    DTOUser user = new DTOUser();
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