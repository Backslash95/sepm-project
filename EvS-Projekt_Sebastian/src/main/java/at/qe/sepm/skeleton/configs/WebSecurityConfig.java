package at.qe.sepm.skeleton.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring configuration for web security.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>
 */

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    
    
    /* Define which URL paths should be secured and which not*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.headers().frameOptions().disable(); // needed for H2 console

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login.xhtml")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
                

        http.authorizeRequests()
                //Permit access to the H2 console
                .antMatchers("/h2-console/**").permitAll()
                //Permit access for all to error pages
                .antMatchers("/error/**")
                .permitAll()
                // Only access with admin role
                .antMatchers("/admin/**")
                .hasAnyAuthority("ADMIN")
                //Permit access only for some roles
                .antMatchers("/secured/**")
                .hasAnyAuthority("ADMIN", "PSLEADER", "STUDENT")
                //If user doesn't have permission, forward him to login page
                .and()
                .formLogin()
                // custom login page, everyone is allowed to view it 
                .loginPage("/login.xhtml")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/secured/welcome.xhtml")
                
                // if user password combination does not match any entry, error message will be displayed
                .failureUrl("/login.xhtml?error=true");

        http.exceptionHandling().accessDeniedPage("/error/access_denied.xhtml");

        http.sessionManagement().invalidSessionUrl("/error/invalid_session.xhtml");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //Configure roles and passwords via datasource
        auth.jdbcAuthentication().dataSource(dataSource)
        		.passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select username, password, enabled from user where username=?")
                .authoritiesByUsernameQuery("select user_username, roles from user_user_role where user_username=?");
    }
    
    /**
     * Returns a PasswordEncoder for jdbcAuthentication()
     * @return PasswordEncoder 
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

}
