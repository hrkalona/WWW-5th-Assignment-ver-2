package BasketVer2;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hrkalona
 */
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.ServletContext;

public class SourceCodeServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("application/rar");
              
        ServletContext ctx = getServletContext();
        InputStream is = ctx.getResourceAsStream("/project3ver2.rar");
        
        int read = 0;
        byte[] bytes = new byte[1024];
        
        OutputStream os;
        try {
            os = response.getOutputStream();
       
        
        while((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();
        
        } 
        catch (IOException ex) {}            

    }

    
}
