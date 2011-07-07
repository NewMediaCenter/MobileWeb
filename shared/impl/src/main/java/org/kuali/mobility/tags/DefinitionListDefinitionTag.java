package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DefinitionListDefinitionTag extends SimpleTagSupport {
    
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DefinitionListDefinitionTag.class);
    
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<dd>");
            getJspBody().invoke(out);          
            out.println("</dd>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
}