package name.babu.qooa.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
public class DTOUser {

  private Long id;
  private String username;
  private String password;
  private String passwordConfirm;
  private Collection<Role> roles;

  private String firstName;
  private String lastName;
  private String email;
  private boolean enabled;
  private boolean tokenExpired;

  // votes --------------
  private Set<String> votedUpQuestionIds = new HashSet<>();
  private Set<String> votedDownQuestionIds = new HashSet<>();
  private Set<String> downVotedAnswerIds = new HashSet<>();
  private Set<String> upVotedAnswerIds = new HashSet<>();

  public void setVotedDownQuestionIds(Set<String> votedDownQuestionIds) {
    this.votedDownQuestionIds = votedDownQuestionIds;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isTokenExpired() {
    return tokenExpired;
  }

  public void setTokenExpired(boolean tokenExpired) {
    this.tokenExpired = tokenExpired;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Transient
  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  @ManyToMany
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  public Collection<Role> getRoles() {
    return roles;
  }

  @ElementCollection
  public Set<String> getVotedUpQuestionIds() {
    return votedUpQuestionIds;
  }

  public void setVotedUpQuestionIds(Set<String> votedUpQuestionIds) {
    this.votedUpQuestionIds = votedUpQuestionIds;
  }

  @ElementCollection
  public Set<String> getVotedDownQuestionIds() {
    return votedDownQuestionIds;
  }
  public void setRoles(Collection<Role> roles) {
    this.roles = roles;
  }

  public void addVotedQuestions(String questionId) {
    votedUpQuestionIds.add(questionId);
  }

  public void removeDownVotedQuestion(String questionId) {
    votedDownQuestionIds.remove(questionId);

  }

  public void addDownvotedQuestion(String questionId) {
    votedDownQuestionIds.add(questionId);
  }

  public void removeUpVotedQuestion(String questionId) {
    votedUpQuestionIds.remove(questionId);
  }

  @ElementCollection
  public Set<String> getDownVotedAnswerIds() {
    return downVotedAnswerIds;
  }

  @ElementCollection
  public Set<String> getUpVotedAnswerIds() {
    return upVotedAnswerIds;
  }

  public void addDowVotedAnswer(String answerId) {
    downVotedAnswerIds.add(answerId);
  }

  public void removeUpVotedAnswer(String answerId) {
    upVotedAnswerIds.remove(answerId);
  }

  public void addUpVotedAnswer(String answerId) {
    upVotedAnswerIds.add(answerId);
  }

  public void removeDownVotedAnswer(String answerId) {
    downVotedAnswerIds.remove(answerId);
  }

  public void setDownVotedAnswerIds(Set<String> downVotedAnswerIds) {
    this.downVotedAnswerIds = downVotedAnswerIds;
  }

  public void setUpVotedAnswerIds(Set<String> upVotedAnswerIds) {
    this.upVotedAnswerIds = upVotedAnswerIds;
  }

}