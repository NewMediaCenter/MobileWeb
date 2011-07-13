package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FormInputTag extends SimpleTagSupport {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ContentTag.class);
    
    private String id;
    private String title;
    private String name;
    private String type;
    
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setName(String name) {
		this.name = name;
	}
    
	public void setType(String type) {
		this.type = type;
	}

	public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<div data-role=\"fieldcontain\" data-theme=\"b\">");
            out.println("<label for=\"" + name + "\">" + title + "</label>");
            out.println("<input type=\"" + type + "\" name=\"" + name + "\" id=\"" + id + "\" value=\"\" />");
            getJspBody().invoke(out);
            out.println("</div>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
