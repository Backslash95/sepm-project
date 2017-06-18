package at.qe.sepm.skeleton.configs;

import java.util.Collection;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.MailService;
import at.qe.sepm.skeleton.services.ProjectService;
import at.qe.sepm.skeleton.services.UserService;

/**
 * Configuration for mail-service.
 *
 * @author Andre Potocnik <andre.potocnik@student.uibk.ac.at>, Elias Jochum <elias.jochum@student.uibk.ac.at>
 */
@Configuration
@EnableScheduling
public class MailServiceConfig {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MailService mailService;
	
	private String host = "smtp.gmail.com";
	
	private Integer port = 587;
	
	private String username = "zeitstempel3000@gmail.com";
	
	private String password = "Z3e0i0t0";
	
	private String protocol = "smtp";
	
	private String tls = "true";
	
	/* Set debug true, to print debugging information on console */
	private String debug = "false";
	
	private String authentication = "true";
		
	/**
	 * Set login details
	 * 
	 * @return javaMailSender managed bean
	 */
	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		
		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setProtocol(protocol);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		
		javaMailSender.setJavaMailProperties(getMailProperties());
		
		return javaMailSender;
	}
	
	/**
	 * Set provider specific properties to establish a connection
	 * 
	 * @return JavaMailProperties
	 */
	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", protocol);
		properties.setProperty("mail.smtp.auth", authentication);
		properties.setProperty("mail.smtp.starttls.required", tls);
		properties.setProperty("mail.smtp.starttls.enable", tls);
		properties.setProperty("mail.debug", debug);
		
		/* 
		 * Should be enabled if SSL connection is desired. (use port 465 for gmail)
		 * properties.put("mail.smtp.socketFactory.port", port);
		 * properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 * properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		 */
		return properties;		
	}
	
	/**
	 * Weekly email report which is fired up every Friday at 18 pm (second,
	 * minute, hour, day of month, month, day(s) of week)
	 */
	@Scheduled(cron = "0 0 18 * * FRI")
	private void weeklyReport() {
		// Alle PSLeader als Collection
		//UserListController userList = new UserListController();
		Collection<User> allPSLeader = userService.getAllPSLeaders();

		for (User user : allPSLeader) {
			// Alle Projekte von bestimmten PSLeader als Collection
			Collection<Project> allProjects = projectService.getProjectsByPSLeader(user);
			StringBuilder text = new StringBuilder();
			text.append("Dear Proseminar Leader, \n\n here you can see the weekly Report of each Project.\n");

			for (Project project : allProjects) {
				text.append("\n");
				text.append("Project: ");
				text.append(project.getProjectName() + ": ");
				text.append(projectService.getMinutesByProject(project));
				text.append(" Minutes\n");
				// Stunden der Studenten
				Collection<User> students = userService.getUsersByProject(project);
				for (User oneStudent : students) {
					text.append(oneStudent.getLastName());
					text.append(" ");
					text.append(oneStudent.getFirstName() + ": ");
					text.append(projectService.getMinutesByProjectAndStudent(project, oneStudent));
					text.append(" Minutes\n");
				}
			}

			text.append(
					"\nAll hours were calculated in milliseconds and rounded up to minutes.\n\nThis is an automated message from Zeitstempel 3000. Please do not reply to this address");

			try {
				mailService.sendMail("zeitstempel3000@gmail.com", user.getEmail(), "Weekly report", text.toString());
			} catch (MailException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public String getHost() {
		return this.host;
	}

	public Integer getPort() {
		return this.port;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return (password == "") ? "not set" : "set";
	}

	public String getProtocol() {
		return this.protocol;
	}
}
