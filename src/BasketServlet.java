/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hrkalona2
 */
package BasketVer2;

import java.io.File;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BasketServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setContentType("text/html");
            
            double[] prices = parsePricesXml();
            
            String[] quantities = new String[3];
            int j;
            
            for(j = 0; j < quantities.length; j++) {
                quantities[j] = request.getParameter("quantity" + (j + 1));
            }
            
            int[] q = new int[quantities.length];
                        
            for(j = 0; j < q.length; j++) {
                try {
                    q[j] =  Integer.parseInt(quantities[j]);
                }
                catch(NumberFormatException ex) {
                    q[j] = 0;
                    quantities[j] = "";
                }
            }
            
            double total = 0;
            double[] totals = new double[quantities.length];
                        
            for(j = 0; j < totals.length; j++) {
                totals[j] = q[j] * prices[j];
                total += totals[j];
            } 
 
            String[] reply = {convertMoneyFormat(totals[0]), 
                              convertMoneyFormat(totals[1]), 
                              convertMoneyFormat(totals[2]), 
                              convertMoneyFormat(total),
                              quantities[0], 
                              quantities[1], 
                              quantities[2],
                              convertMoneyFormat(prices[0]),
                              convertMoneyFormat(prices[1]), 
                              convertMoneyFormat(prices[2])};
            
            
            Cookie cookie = new Cookie("users_basket", reply[4] + "/" + reply[5] + "/" + reply[6] + "/");
            cookie.setMaxAge(1000 * 60);
            response.addCookie(cookie);
                     
            request.setAttribute("styles", reply);
            RequestDispatcher view = request.getRequestDispatcher("formver2.jsp");
            view.forward(request, response);
           
        }
        catch (ServletException ex) {}
        catch (IOException ex) {}
            
           

    }
    
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setContentType("text/html");
            
            Cookie[] cookies = request.getCookies();
            
            String users_basket = "";
            
            double[] prices = parsePricesXml();
            
            String[] reply = {"0.00",
                              "0.00",
                              "0.00",
                              "0.00",
                              "",
                              "",
                              "",
                              convertMoneyFormat(prices[0]),
                              convertMoneyFormat(prices[1]), 
                              convertMoneyFormat(prices[2])};
            
            
            
            if(cookies != null) {
                
                for(int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if(cookie.getName().equals("users_basket")) {
                        users_basket = cookie.getValue();
                        String[] quantities = extractCookies(users_basket);
                                              
                        int[] q = new int[quantities.length];
                        
                        int j;
                                
                        for(j = 0; j < q.length; j++) {
  
                            try {
                                reply[j + 4] = "" + (q[j] =  Integer.parseInt(quantities[j]));
                            }
                            catch(NumberFormatException ex) {
                                q[j] = 0;
                            }
                        }
            
                        double total = 0;
                        double[] totals = new double[quantities.length];
                        
                        for(j = 0; j < totals.length; j++) {
                            totals[j] = q[j] * prices[j];
                            total += totals[j];
                        } 
                        
                        for(j = 0; j < totals.length; j++) {
                            reply[j] = convertMoneyFormat(totals[j]); 
                        }
                        
                        reply[j] = convertMoneyFormat(total);
                       
                        break;
                    }
                }
                
            }
            
            
         
            request.setAttribute("styles", reply);
            RequestDispatcher view = request.getRequestDispatcher("formver2.jsp");
            view.forward(request, response);
           
        }
        catch (ServletException ex) {}
        catch (IOException ex) {}
            
           

    }
    
    private String convertMoneyFormat(double total) {
        
        int total1 = (int)total;
        String temp = "" + total1;
        String converted_total1 = "";

        int number_of_dots = temp.length() / 3;

        if(temp.length() % 3 == 0) {
            number_of_dots--;
        }

        if(number_of_dots > 0) {
            int i = temp.length() - 1;

            for(int counter = 0; number_of_dots > 0; i--) {
                converted_total1 = temp.charAt(i) + converted_total1;
                counter++;
                if(counter == 3) {
                    converted_total1 = "," + converted_total1;
                    counter = 0;
                    number_of_dots--;
                }
            }

            while(i >= 0) {
                converted_total1 = temp.charAt(i) + converted_total1;
                i--;
            } 

        }
        else {
            converted_total1 = temp;
        }
        
        String converted_total2 = "" + total + "0";
        
        int i;
        for(i = 0; converted_total2.charAt(i) != '.'; i++) {}
        
        return converted_total1 + converted_total2.substring(i, i + 3);
        
      
    }
      
    private String[] extractCookies(String cookie) {
        
        int counter = 0;
        for(int j = 0; j < cookie.length(); j++) {
            if(cookie.charAt(j) == '/') {
                counter++;   
            }
        }
        
        String [] reply = new String[counter];
        
        int i = 0, j = 0;
        while(i < cookie.length()) {
            int temp = i;
            for(; cookie.charAt(i) != '/'; i++) {}
            reply[j] = cookie.substring(temp, i);
            j++;
            i++;
        }
        
        return reply;
        
    }
    
    private double[] parsePricesXml() {
        
        double[] prices = null;
        
        try {
          
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("../webapps/BasketVer2/price.xml"));

            doc.getDocumentElement ().normalize ();
                       
            NodeList list_of_products = doc.getElementsByTagName("Product");
            
            prices = new double[list_of_products.getLength()];
                        
            for(int i = 0; i < list_of_products.getLength() ; i++){
                Node first_product_node = list_of_products.item(i);
                            
                if(first_product_node.getNodeType() == Node.ELEMENT_NODE){

                    Element first_product_element = (Element)first_product_node;
                    NodeList name_list = first_product_element.getElementsByTagName("Price");
                    Element  name_element = (Element)name_list.item(0);

                    NodeList textFNList = name_element.getChildNodes();
                   
                    prices[i] = Double.valueOf(((Node)textFNList.item(0)).getNodeValue().trim());

                }
            }
                        
        }
        catch(ParserConfigurationException pce) {}
        catch(SAXException se) {}
        catch(IOException ioe) {}
        
        return prices;
        
    }
             
}
