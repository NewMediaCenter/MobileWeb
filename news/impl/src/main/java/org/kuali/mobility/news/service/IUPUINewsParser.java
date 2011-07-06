package org.kuali.mobility.news.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.kuali.mobility.news.entity.JdomXmlParser;
import org.kuali.mobility.news.entity.Rss;
import org.kuali.mobility.news.entity.RssItem;
import org.kuali.mobility.news.entity.RssModel;

public class IUPUINewsParser extends JdomXmlParser {
	
//	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(IUPUINewsParser.class);

	@SuppressWarnings("unchecked")
	public RssModel processList(String url) {
		Rss rss = new Rss();
		List<RssItem> rssItems = new ArrayList<RssItem>();
		RssItem rssItem = null;
		try {
			Document doc = super.retrieveDocumentFromUrl(url, 5000, 5000);
			Element root = doc.getRootElement();
			List<Element> releases = root.getChildren("release");
			for (Element release : releases) {
				rssItem = new RssItem();
				String id = release.getChildText("id");
//				String imageUrl = release.getChildText("image");
				String title = release.getChildText("headline");
				rssItem.setLink(id);
				rssItem.setTitle(title);
				rssItems.add(rssItem);
			}
			rss.setRssItems(rssItems);
		} catch (JDOMException e) {
//			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
//			LOG.error(e.getMessage(), e);
		}
        RssModel model = new RssModel();
        model.setRss(rss);
        return model;
	}
	
    public String process(String link) {
    	String html;
    	String prefix = "http://newscenter.iupui.edu/";
    	if (link.indexOf(prefix) > -1) {
    		link = link.substring(prefix.length());
    		if (link.indexOf('/') > -1) {
    			String code = link.substring(0, link.indexOf('/'));
    			try {
    				html = this.parse(code);
    				html = StringEscapeUtils.escapeHtml(html);
    				html = html.trim();
    				html = html.replaceAll("\n", "<br/><br/>");
    			} catch (Exception e) {
    				html = "";
    			}
    		} else {
    			html = "";
    		}
    	} else {
    		html = "";
    	}
    	return html;
    }
    
	public String parse(String code) {
		String story = null;
		String url = "http://newscenter.iupui.edu/?c=releaseDisplayXML&id=" + code;
		try {
			Document doc = super.retrieveDocumentFromUrl(url, 5000, 5000);
			Element root = doc.getRootElement();
			Element release = root.getChild("release");
			story = release.getChildText("copy");
		} catch (JDOMException e) {
//			LOG.error(e.getMessage(), e);
			story = "This story is currently unavailable.";
		} catch (IOException e) {
//			LOG.error(e.getMessage(), e);
			story = "This story is currently unavailable.";
		}
		return story;
	}
}
