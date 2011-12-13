/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hrkalona
 */
package BasketVer2;

import javax.servlet.http.*;
import java.io.*;

public class InformationServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.sendRedirect(((RedirectLink)getServletContext().getAttribute("redirect_link")).getRedirectLink());          
        }
        catch (IOException ex) {}
            
           

    }

    
}
