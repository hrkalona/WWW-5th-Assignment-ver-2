package BasketVer2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hrkalona
 */
public class InformationServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        ServletContext sc = sce.getServletContext();
               
        sc.setAttribute("redirect_link", new RedirectLink(sc.getInitParameter("redirectLink3")));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
    
}
