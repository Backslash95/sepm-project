package sepm.creche.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring configuration for web security.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
@Configuration
@EnableWebSecurity()

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final SessionRegistry SESSION_REGISTRY = new SessionRegistryImpl();

	@Autowired
	DataSource dataSource;

	private PasswordEncoder encoder;

	public WebSecurityConfig() {
		encoder = new StandardPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.headers().frameOptions().disable(); // needed for H2 console

		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true)
				.logoutSuccessUrl("/login.xhtml");

		http.authorizeRequests()
				// Permit access to the H2 console
				.antMatchers("/h2-console/**").permitAll()
				// Permit access for all to error pages
				.antMatchers("/error/**").permitAll()
				// Only access with admin role
				.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
				// Only access with admin role
				.antMatchers("/employee/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
				// Permit access only for some roles
				.antMatchers("/secured/**").hasAnyAuthority("ADMIN", "EMPLOYEE", "PARENT", "INACTIVEPARENT")
				.antMatchers("/secured/restricted/**").hasAnyAuthority("ADMIN", "EMPLOYEE", "PARENT", "INACTIVEPARENT")
				.antMatchers("/secured/other/**").hasAnyAuthority("ADMIN", "EMPLOYEE", "PARENT")
				.antMatchers("/secured/other/Employee/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
				.antMatchers("/secured/other/Parent/**").hasAnyAuthority("ADMIN", "PARENT")
				// If user doesn't have permission, forward him to login page
				.and().formLogin().loginPage("/login.xhtml").loginProcessingUrl("/login")
				.defaultSuccessUrl("/secured/welcome.xhtml").failureUrl("/login.xhtml?error");

		http.exceptionHandling().accessDeniedPage("/error/denied.xhtml");

		http.sessionManagement().invalidSessionUrl("/login.xhtml");

		/* Limit to 1 login per User */
		http.sessionManagement().maximumSessions(1).sessionRegistry(SESSION_REGISTRY)
				.expiredUrl("/error/invalid_session.xhtml");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Configure roles and passwords via datasource
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from user where username=?")
				.authoritiesByUsernameQuery("select user_username, roles from user_user_role where user_username=?")
				.passwordEncoder(encoder);

	}

	@Bean
	public static SessionRegistry sessionRegistry() {
		return SESSION_REGISTRY;
	}

}
