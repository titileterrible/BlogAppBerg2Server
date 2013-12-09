
package filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author GLYSE581B
 */
@WebFilter(filterName = "RequestFilter", urlPatterns = {"/webresources/*"})
public class RequestFilter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("doFilter");
        HttpServletResponse res = (HttpServletResponse) response;
        //Dire qu'on accepte des requetes de n'importe quel domaine sur les ressources gérés par la servlet
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "PUT,GET,DELETE,POST,OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "Content-type, Accept,Origin");
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Init");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void destroy() {
        System.out.println("destroy");
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}