package org.kuali.mobility.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ListViewTag extends SimpleTagSupport {
    
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ListViewTag.class);
    
    private String id;
    private boolean filter;
    private String dataTheme;
    private String dataDividerTheme;
    private boolean dataInset;
    private String cssClass;
    
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<ul data-role=\"listview\"" + (id != null && !"".equals(id.trim()) ? " id=\"" + id.trim() + "\"" : "") + (cssClass != null && !"".equals(cssClass.trim()) ? " class=\"" + cssClass.trim() + "\"" : "") + (dataTheme != null && !"".equals(dataTheme.trim()) ? " data-theme=\"" + dataTheme.trim() + "\"" : "") + "\" data-inset=\""+ (dataInset ? "true" : "false") + "\" data-filter=\"" + (filter ? "true" : "false") + "\"" + (dataDividerTheme != null && !"".equals(dataDividerTheme.trim()) ? " data-dividertheme=\"" + dataDividerTheme + "\"" : "") + ">");
            getJspBody().invoke(out);          
            out.println("</ul>");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setDataTheme(String dataTheme) {
        this.dataTheme = dataTheme;
    }

    public void setDataDividerTheme(String dataDividerTheme) {
        this.dataDividerTheme = dataDividerTheme;
    }
    
    public void setFilter(boolean filter) {
        this.filter = filter;
    }
    
    public void setDataInset(boolean dataInset) {
        this.dataInset = dataInset;
    }
    
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
}	