package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ContentTag extends SimpleTagSupport {
    
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ContentTag.class);
   
    private String dataTheme;
    private String cssClass;
    
    
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<div data-role=\"content\" data-theme=\"" + (dataTheme != null && !"".equals(dataTheme.trim()) ? dataTheme : "b") + "\"" + (cssClass != null && !"".equals(cssClass.trim()) ? " class=\"" + cssClass + "\"" : "") + ">");
            getJspBody().invoke(out);          
            out.println("</div>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    public void setDataTheme(String dataTheme) {
        this.dataTheme = dataTheme;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    
}