package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DefinitionListViewTag extends SimpleTagSupport {
    
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DefinitionListViewTag.class);
    
    private String id;
    private boolean filter;

    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<dl data-role=\"listview\" data-theme=\"b\" data-dividertheme=\"b\" data-filter=\""+ (filter ? "true" : "false") + "\" data-inset=\"false\" id=\"" + id + "\">");
            getJspBody().invoke(out);          
            out.println("</div>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }
    
}