package name.babu.qooa.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private SimpleAuthenticationSuccessHandler successHandler;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable() // TODO resolve this
        .headers().frameOptions().sameOrigin().and() // TODO solve this
        .authorizeRequests()
          .antMatchers("/h2-console/*")
            .permitAll()
          .antMatchers("/", "/home", "/login", "/logout", "/error", "/registration", "/questions")
            .permitAll()
          .antMatchers("/css/**")
            .permitAll()
           .antMatchers("/skins/**")
            .permitAll()
          .antMatchers("/tmp/**")
            .permitAll()
          .anyRequest()
            .authenticated()
            .and()
          .formLogin()
            .loginPage("/login")
            .successHandler(successHandler)
            .permitAll()
            .and()
          .logout()
            .logoutSuccessUrl("/home")
            .permitAll();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}