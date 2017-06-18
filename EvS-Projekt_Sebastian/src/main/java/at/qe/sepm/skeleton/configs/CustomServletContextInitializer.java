package at.qe.sepm.skeleton.configs;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for servlet context.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>
 */
@SuppressWarnings("deprecation")
@Configuration
public class CustomServletContextInitializer implements ServletContextInitializer {

	//TODO Change project stage if project is finished!
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        sc.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        //TODO Development
        sc.setInitParameter("javax.faces.PROJECT_STAGE", "Production");
    }

}
