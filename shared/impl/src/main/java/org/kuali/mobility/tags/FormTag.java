package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FormTag extends SimpleTagSupport {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ContentTag.class);
    
    private String id;
    private String method;
    private String action;
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setMethod(String name) {
		this.method = name;
	}
    
	public void setAction(String type) {
		this.action = type;
	}

	public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<form action=\"" + action + "\" method=\"" + method + "\"/>");
            getJspBody().invoke(out);
            out.println("</form>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
	
}
