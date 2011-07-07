package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PageTag extends SimpleTagSupport {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PageTag.class);

    private String id;
    private String title;
    
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + title + "</title>");
            out.println("<link href=\"/mdot/css/jquery.mobile-1.0b1.css\" rel=\"stylesheet\" type=\"text/css\" />");
            out.println("<link href=\"/mdot/css/custom.css\" rel=\"stylesheet\" type=\"text/css\" />");
            out.println("<script type=\"text/javascript\" src=\"/mdot/js/jquery-1.6.1.min.js\"></script>");
            out.println("<script type=\"text/javascript\" src=\"/mdot/js/custom.js\"></script>");
            out.println("<script type=\"text/javascript\" src=\"/mdot/js/jquery.mobile-1.0b1.js\"></script>");
            out.println("<script type=\"text/javascript\" src=\"/mdot/js/jquery.tmpl.js\"></script>");
            out.println("<script type=\"text/javascript\" src=\"http://maps.google.com/maps/api/js?sensor=true\"></script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div data-role=\"page\" id=\"" + id + "\">");
            out.println("<div data-role=\"header\">");
            out.println("<h1>" + title + "</h1>");
            out.println("</div>");
            getJspBody().invoke(out);
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}