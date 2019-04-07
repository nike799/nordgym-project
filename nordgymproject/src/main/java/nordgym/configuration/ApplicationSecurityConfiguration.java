package nordgym.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final static String[] PERMITTED_ROUTES_ANONYMOUS = {"/","/login","/gallery","/contacts","/trainings/advanced","/trainings/beginners"};
    private final static String[] PERMITTED_ROUTES_USER = {"/home","/logout","/user-profile/*"};
    private final static String[] PERMITTED_ROUTES_ADMIN = {"/admin/home","/edit-user/*","/user-profile/**", "/register/*"};
    private final static String[] PERMITTED_ROUTES_CSS_JS = {"/css/**", "/js/**","/bootstrap/**"};
    private final UserDetailsService userDetailsService;

    @Autowired
    public ApplicationSecurityConfiguration(@Qualifier("userServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .and()
                .authorizeRequests()
                .antMatchers(PERMITTED_ROUTES_CSS_JS).permitAll()
                .antMatchers(PERMITTED_ROUTES_ANONYMOUS).permitAll()
                .antMatchers(PERMITTED_ROUTES_USER).hasAnyAuthority("ADMIN", "USER")
                .antMatchers(PERMITTED_ROUTES_ADMIN).hasAuthority("ADMIN")
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
                .tokenValiditySeconds(10000)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/unauthorized")
                .and();
    }
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}

