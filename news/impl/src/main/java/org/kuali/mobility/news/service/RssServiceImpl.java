package org.kuali.mobility.news.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.kuali.mobility.news.entity.Rss;
import org.kuali.mobility.news.entity.RssCategory;
import org.kuali.mobility.news.entity.RssItem;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.rss.Category;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Enclosure;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.feed.rss.TextInput;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.io.ParsingFeedException;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.XmlReader;

@Service
public class RssServiceImpl implements RssService {
	
	//TODO:Where do we put constants?
	private static String WEB_APP_CONTEXT_PATH = "/mobile";
	private static String RSS_SOCKET_TIMEOUT_SECONDS = "RSS.SOCKET.TIMEOUT.SECONDS";
	private static int RSS_SOCKET_DEFAULT_TIMEOUT = 10000;

	public Rss fetch(String url) throws Exception {
		Rss rss = parseRssFeed(url);
		return rss;
	}
	
	private Rss parseRssFeed(String url) throws Exception {
        WireFeed wireFeed;
        WireFeedInput input = new WireFeedInput();
        GetMethod get = null;
        try {
            //LOG.info("SSL rss preparation: " + url);
            //SSLUtility.prepareHttps(url); TODO:do we need this?
            get = new GetMethod(url);
            int timeout = getSocketTimeout(RSS_SOCKET_TIMEOUT_SECONDS, RSS_SOCKET_DEFAULT_TIMEOUT);
            XmlReader xmlReader = new XmlReader(getInputStreamFromGetMethod(get, timeout));
            wireFeed = input.build(xmlReader);
        } catch (ParsingFeedException e) {
            //LOG.info("RSS parsing failed for URL:" + url + " ... trying alternative method.");
            //SSLUtility.prepareHttps(url);
            String xmlString = getStringFromRssUrl(url);
            ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes());
            XmlReader xmlReader = new XmlReader(stream);
            wireFeed = input.build(xmlReader);
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return convertRssContent(wireFeed, url);
    }
	
	private String getStringFromRssUrl(String url) throws Exception {
        //LOG.info("start rssConnection:" + url);
        BufferedReader br = null;
        GetMethod get = null;
        StringBuilder sb = new StringBuilder();
        try {
            get = new GetMethod(url);
            int timeout = getSocketTimeout(RSS_SOCKET_TIMEOUT_SECONDS, RSS_SOCKET_DEFAULT_TIMEOUT);
            br = new BufferedReader(new InputStreamReader(getInputStreamFromGetMethod(get, timeout)));
            String line = null;
            //LOG.info("start reading rssLines:" + url);
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            //LOG.info("end getting rssLines:" + url);
        } catch (Exception e) {
            //LOG.error("Error in getStringFromRssUrl for url " + url, e);
        } finally {
            if (br != null) {
                br.close();
            }
            if (get != null) {
                get.releaseConnection();
            }
        }
        //LOG.info("end rssConnection:" + url);
        return xmlFixer(sb.toString());
    }
	
	private String xmlFixer(String xmlString) {
        xmlString = xmlString.replaceAll("&([^; ]*);", "||VALIDXMLTAG||$1;");
        xmlString = xmlString.replaceAll("&", "&amp;");
        xmlString = xmlString.replaceAll("\\|\\|VALIDXMLTAG\\|\\|([^; ]*);", "&$1;");
        return (xmlString);
    }
	
	public Rss convertRssContent(WireFeed wireFeed, String url) throws Exception {
    	/*
    	 * Escaping has been removed from here. It should be handled in the view.
    	 */
        Channel channel = (Channel) wireFeed;

        Rss rss = new Rss();
        rss.setUrl(url);
        String title = channel.getTitle();
        if (title != null) {
//            title = StringEscapeUtils.escapeHtml(title).trim();
        	title = title.trim();
        }
        rss.setTitle(title);
        String channelLink = "";
        if (channel.getLink() != null) {
            channelLink = channel.getLink().trim();
        }
        rss.setLink(channelLink);
        if (channel.getImage() != null && channel.getImage().getUrl() != null) {
            String image = channel.getImage().getUrl();
            if (!channel.getImage().getUrl().toLowerCase().startsWith("https")) {
                image = image.replaceAll("http:", WEB_APP_CONTEXT_PATH + "/proxy.serv?remote_url=http:");
            }
            rss.setImageLocation(image);
        }
        List<RssItem> rssItems = new ArrayList<RssItem>();
        int counter = 1;
        //LOG.info("start getting rssItems:" + url);
        for (@SuppressWarnings("unchecked")
		Iterator<Item> iter = channel.getItems().iterator(); iter.hasNext();) {
            Item item = iter.next();
            String link = item.getLink();
            String enclosureUrl = null;
            String enclosureType = null;
            Long enclosureLength = null;
            boolean useEnclosureUrlAsLink = false;
            if (link == null) {
            	useEnclosureUrlAsLink = true;
            }
            if (item.getEnclosures() != null && item.getEnclosures().size() > 0 && item.getEnclosures().get(0) != null) {
                if (item.getEnclosures().get(0) instanceof SyndEnclosure) {
                    SyndEnclosure enclosure = (SyndEnclosure) item.getEnclosures().get(0);
                    if (enclosure.getUrl() != null && useEnclosureUrlAsLink) {
                        link = enclosure.getUrl();
                    }
                    enclosureUrl = enclosure.getUrl();
                    enclosureType = enclosure.getType();
                    enclosureLength = enclosure.getLength();
                } else if (item.getEnclosures().get(0) instanceof Enclosure) {
                    Enclosure enclosure = (Enclosure) item.getEnclosures().get(0);
                    if (enclosure.getUrl() != null && useEnclosureUrlAsLink) {
                        link = enclosure.getUrl();
                    }
                    enclosureUrl = enclosure.getUrl();
                    enclosureType = enclosure.getType();
                    enclosureLength = enclosure.getLength();
                }
            }

            String itemTitle = item.getTitle();
            if (itemTitle != null) {
//                itemTitle = StringEscapeUtils.escapeHtml(itemTitle).trim();
            	itemTitle = itemTitle.trim();
            }

            RssItem rssItem = new RssItem();
            rssItem.setRss(rss);
            rssItem.setEnclosureLength(enclosureLength);
            rssItem.setEnclosureType(enclosureType);
            rssItem.setEnclosureUrl(enclosureUrl);
            rssItem.setOrder(new Long(counter++));
            rssItem.setLink(link);
            rssItem.setTitle(itemTitle);
            if (item.getPubDate() != null) {
                // pubDate will be null if there is no pubDate tag for the item. This can happen if the feed uses an unrecognized name for a
                // date tag, or neglects to add one.
                Timestamp ts = new Timestamp(item.getPubDate().getTime());
                rssItem.setPublishDate(ts);
            }
            if (item != null && item.getDescription() != null && item.getDescription().getValue() != null) {
                String itemDescription = item.getDescription().getValue();
//                itemDescription = StringEscapeUtils.escapeHtml(itemDescription).trim();
                itemDescription = itemDescription.trim();
                rssItem.setDescription(itemDescription.trim());
            }

            rssItem.setCategories(new ArrayList<RssCategory>());
            if (item != null && item.getCategories() != null) {
//                LOG.info("start getting rssCategory:" + url);
                for (@SuppressWarnings("unchecked")
				Iterator<Category> iterator = item.getCategories().iterator(); iterator.hasNext();) {
                    Category category = iterator.next();
                    RssCategory rssCategory = new RssCategory();
                    rssCategory.setDomain(category.getDomain());
                    rssCategory.setValue(category.getValue());
                    rssItem.getCategories().add(rssCategory);
                }
//                LOG.info("end getting rssCategory:" + url);
            }

            rssItems.add(rssItem);
        }
        //LOG.info("end getting rssItems:" + url);
        rss.setRssItems(rssItems);
        TextInput textInput = channel.getTextInput();
        if (textInput != null) {
            rss.setFormLink(textInput.getLink());

            String textInputDescription = "no description";
            if (textInput.getDescription() != null) {
//                textInputDescription = StringEscapeUtils.escapeHtml(textInput.getDescription()).trim();
                textInputDescription = textInput.getDescription().trim();
            }
            rss.setFormDescription(textInputDescription);
            rss.setFormName(textInput.getName());

            String textInputTitle = "no title";
            if (textInput.getTitle() != null) {
//                textInputTitle = StringEscapeUtils.escapeHtml(textInput.getTitle()).trim();
                textInputTitle = textInput.getTitle().trim();
            }
            rss.setFormTitle(textInputTitle);
        }
        rss.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        try {
            //String name = InetAddress.getLocalHost().getHostName();
            //LOG.info("[" + name + "] Finished downloading url: " + url);
        } catch (Exception e) {
            // Ignore exceptions if logging or hostname lookup fails.
        }
        return rss;
    }
	
	private InputStream getInputStreamFromGetMethod(GetMethod get, int timeout) throws Exception {
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setParameter(HttpClientParams.SO_TIMEOUT, new Integer(timeout));
        httpClient.getParams().setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, new Long(timeout));
        httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, new Integer(timeout));
        int status = httpClient.executeMethod(get);
        if (status == HttpStatus.SC_OK) {
        	return get.getResponseBodyAsStream();	
        }
        return null;
    }
	
	private Integer getSocketTimeout(String name, int defaultTimeoutInMillis) {
        int timeout = defaultTimeoutInMillis;
        try {
//            String param = cacheService.findConfigParamValueByName(name);
//            if (param != null && !"".equals(param)) {
//                timeout = new Integer(param) * 1000;
//            }
        } catch (Exception e) {
            // Use default
        }
        return timeout;
    }
}
