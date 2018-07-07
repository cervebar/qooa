package name.babu.qooa.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import name.babu.qooa.user.DTOUser;
import name.babu.qooa.user.Role;
import name.babu.qooa.user.repository.RoleRepository;
import name.babu.qooa.user.repository.UserRepository;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    DTOUser user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Username " + username + "not found");
    }
    // TODO add acount not expired ,credentials not expired etc...
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        user.isEnabled(), true, true, true, getGrantedAuthorities(user.getRoles()));
  }

  private List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    roles.forEach(role -> role.getPrivileges()
        .forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName()))));
    return authorities;
  }

}