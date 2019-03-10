package nordgym.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final static String[] PERMITTED_ROUTES = {"/", "/login", "/edit-user/**", "/edit-user", "/register", "/logout", "/css/**", "/js/**", "/bootstrap/**"};
    private final UserDetailsService userDetailsService;

    @Autowired
    public ApplicationSecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(PERMITTED_ROUTES).permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/home/**").hasAnyAuthority("ADMIN", "USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .rememberMeCookieName("rememberMeCookie")
                .key("hardaway")
                .userDetailsService(this.userDetailsService)
                .tokenValiditySeconds(1000)
                .and()
                .logout().logoutSuccessUrl("/login").permitAll()
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/unauthorized")
                .and();

    }
}

