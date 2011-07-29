package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ListItemTag extends SimpleTagSupport {
    
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ListItemTag.class);
    
    private String dataRole;
    private String dataTheme;
    private String specialClass;
    
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<li " + (dataRole != null && !"".equals(dataRole.trim()) ? " data-role=\"" + dataRole.trim() + "\"" : "") + " data-theme=\"" + (dataTheme != null && !"".equals(dataTheme.trim()) ? dataTheme : "c") + "\">");
            getJspBody().invoke(out);          
            out.println("</li>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    public void setDataRole(String dataRole) {
        this.dataRole = dataRole;
    }
    
    public void setDataTheme(String dataTheme) {
        this.dataTheme = dataTheme;
    }
}