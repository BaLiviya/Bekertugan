package kz.rbots.bekertugan.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 *
 * @author KUYLIM
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().withUser("user")
                .password("123").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/THEME", "/VAADIN/**", "/PUSH/**", "/UIDL/**", "/login", "/error/**", "/access-denied/**", "/vaadinServlet/**", "/forbidden", "/logv2").permitAll()
                .antMatchers("/admin", "/bot-dialog").hasRole("ADMIN");

        http.csrf().disable();
//        http.exceptionHandling().;
//        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl());

      http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
    }

}
