package org.kuali.mobility.news.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringEscapeUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.news.dao.RssDao;
import org.kuali.mobility.news.entity.LinkFeed;
import org.kuali.mobility.news.entity.MaintRss;
import org.kuali.mobility.news.entity.Rss;
import org.kuali.mobility.news.entity.RssCategory;
import org.kuali.mobility.news.entity.RssItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
	
	@Autowired
	private RssDao rssDao;
	
	@Autowired
	private ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
	
	private static final String WEB_APP_CONTEXT_PATH = "/mobile";
	private static final String RSS_SOCKET_TIMEOUT_SECONDS = "IU_RSS_SOCKET_TIMEOUT_SECONDS";
	private static final int RSS_SOCKET_DEFAULT_TIMEOUT = 10000;
	private static final String IU_NEWS_URL_MATCHER_REGEX = "IU_NEWS_URL_MATCHER_REGEX";
	private static final String IU_NEWS_URL_MATCHER_FORMAT = "IU_NEWS_URL_MATCHER_FORMAT";
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RssServiceImpl.class);

	public Rss fetch(String url) throws Exception {
		Rss rss = parseRssFeed(url);
		return rss;
	}
	
	public Rss getRss(MaintRss maintRss, boolean useCache, Rss cachedRss) {
		if (maintRss == null) {
			return null;
		}
		String url = maintRss.getUrl();
		Rss rssFromDatabase = null;
		Long intervalMinute = maintRss.getIntervalMinute();
//		Long intervalMinute = 3L;
		if (intervalMinute == null) {
			intervalMinute = 60L;
		}
		Rss rss = null;
		// If we're going to use the cached version, use the cached version.
		if (useCache) {
			rss = cachedRss;
		}
		boolean cached = rss != null;
		if (cached) {
			rss.setPutInCache(false);
			rss.setUpdate(false);
			rss.setDelete(false);
		}
		// If we're not using the cached version, try to retrieve it from the database first.
		if (!cached) {
			rss = getRssFromDatabase(maintRss.getRssId());
			rssFromDatabase = rss;
			if (rss != null) {
				rss.setUpdate(false);
				rss.setDelete(false);
			}
		}
		Calendar nowMinusHour = Calendar.getInstance();
		nowMinusHour.add(Calendar.MINUTE, -intervalMinute.intValue());
		
		// Thinking about switching the if below to this one so it doesn't fetch on startup
		if (useCache && (rss == null || rss.getLastUpdateDate().before(nowMinusHour.getTime()))) {
		
		// If we don't have an RSS available yet, whether by cache or DB, or the RSS has to be updated:
//		if (rss == null || rss.getLastUpdateDate().before(nowMinusHour.getTime())) {
			if (cached) {
//				rssFromDatabase = null;
				rss = getRssFromDatabase(maintRss.getRssId());
				if (rssFromDatabase == null || rssFromDatabase.getLastUpdateDate().before(nowMinusHour.getTime())) {
					try {
						rss = fetch(url);
					} catch (Exception e) {
						LOG.error("Could not get RSS for url: " + url, e);
						rss = null;
					}
					if (rss != null) {
						rss.setPutInCache(true);
						rss.setDelete(true);
						rss.setUpdate(true);
					} else {
						if (rssFromDatabase != null) {
							rssFromDatabase.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
							rss = rssFromDatabase;
							rss.setDelete(false);
							rss.setPutInCache(true);
							rss.setUpdate(true);
						}
					}
				} else {
					rss = rssFromDatabase;
					rss.setPutInCache(true);
				}
			} else {
				try {
					rss = fetch(url);
					if (rssFromDatabase != null) {
						rss.setRssId(rssFromDatabase.getRssId());
						rss.setVersionNumber(rssFromDatabase.getVersionNumber());
					}
				} catch (Exception e) {
					LOG.error("Could not get RSS for url: " + url, e);
				}
				if (rss != null) {
					rss.setPutInCache(true);
					rss.setUpdate(true);
					rss.setDelete(true);
				}
			}
		}
		if (rss != null) {
			rss.setRssMaintId(maintRss.getRssId());
		}
		return rss;
	}
	
	private Rss getRssFromDatabase(Long maintRssId) {
		Rss rss = null;
		try {
			rss = rssDao.findRssByMaintRssId(maintRssId);
		} catch (NoResultException e) {
			// Not a problem.
		} catch (Exception e) {
			LOG.error("Error loading RSS from database: ", e);
		}
		return rss;
	}
	
	private Rss parseRssFeed(String url) throws Exception {
        WireFeed wireFeed;
        WireFeedInput input = new WireFeedInput();
        GetMethod get = null;
        try {
            LOG.info("SSL rss preparation: " + url);
            //SSLUtility.prepareHttps(url); TODO:do we need this?
            get = new GetMethod(url);
            int timeout = getSocketTimeout(RSS_SOCKET_TIMEOUT_SECONDS, RSS_SOCKET_DEFAULT_TIMEOUT);
            XmlReader xmlReader = new XmlReader(getInputStreamFromGetMethod(get, timeout));
            wireFeed = input.build(xmlReader);
        } catch (ParsingFeedException e) {
            LOG.info("RSS parsing failed for URL:" + url + " ... trying alternative method.");
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
        LOG.info("start rssConnection:" + url);
        BufferedReader br = null;
        GetMethod get = null;
        StringBuilder sb = new StringBuilder();
        try {
            get = new GetMethod(url);
            int timeout = getSocketTimeout(RSS_SOCKET_TIMEOUT_SECONDS, RSS_SOCKET_DEFAULT_TIMEOUT);
            br = new BufferedReader(new InputStreamReader(getInputStreamFromGetMethod(get, timeout)));
            String line = null;
            LOG.info("start reading rssLines:" + url);
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            LOG.info("end getting rssLines:" + url);
        } catch (Exception e) {
            LOG.error("Error in getStringFromRssUrl for url " + url, e);
        } finally {
            if (br != null) {
                br.close();
            }
            if (get != null) {
                get.releaseConnection();
            }
        }
        LOG.info("end rssConnection:" + url);
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
        LOG.info("start getting rssItems:" + url);
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
                LOG.info("start getting rssCategory:" + url);
                for (@SuppressWarnings("unchecked")
				Iterator<Category> iterator = item.getCategories().iterator(); iterator.hasNext();) {
                    Category category = iterator.next();
                    RssCategory rssCategory = new RssCategory();
                    rssCategory.setDomain(category.getDomain());
                    rssCategory.setValue(category.getValue());
                    rssItem.getCategories().add(rssCategory);
                }
                LOG.info("end getting rssCategory:" + url);
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
            String name = InetAddress.getLocalHost().getHostName();
            LOG.info("[" + name + "] Finished downloading url: " + url);
        } catch (Exception e) {
            // Ignore exceptions if logging or hostname lookup fails.
        }
        return rss;
    }
	
	public LinkFeed parseLinkFeedFromUrl(LinkFeed lf) {
//    	LinkFeed lf = new LinkFeed(id, url);
		String html = "";
		String richHtml = "";
		String title = "";
		String url = lf.getFeedUrl();

		GetMethod get = null;
		boolean tryAgainIfNeeded = true;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
	        get = new GetMethod(url);
	        int timeout = getSocketTimeout(RSS_SOCKET_TIMEOUT_SECONDS, RSS_SOCKET_DEFAULT_TIMEOUT);
	        InputStream inputStream = getInputStreamFromGetMethod(get, timeout);
	        InputSource is = new InputSource(inputStream);
			Document doc = builder.parse(is);
			XPathFactory fac = XPathFactory.newInstance();
			XPath xpath = fac.newXPath();
			// Handle body content
			html = this.getStringFromDocumentXpath(doc, "/rss/channel/bodyContent", xpath); // Copenhagen
			if (html.isEmpty()) {
				html = this.getStringFromDocumentXpath(doc, "/rss/channel/description/text()", xpath); // UITS News
			}
			if (html.isEmpty()) {
				html = this.getStringFromDocumentXpath(doc, "/rss/channel/item[1]/description/text()", xpath); // IU Northwest	
			}
			html = prepareHtmlForViewing(html);
			// Handle rich body content
			try {
				richHtml = this.getStringFromDocumentXpath(doc, "/rss/channel/richBodyContent/text()", xpath);
			} catch (Exception e) {
				
			}
			// Handle story title
			title = this.getStringFromDocumentXpath(doc, "/rss/channel/title", xpath); // Copenhagen
			if (url.startsWith("http://www.iun.edu")) {
				title = this.getStringFromDocumentXpath(doc, "/rss/channel/item[1]/title", xpath); // IU Northwest
			}
		} catch (ConnectTimeoutException e) {
			tryAgainIfNeeded = false;
		} catch (SocketTimeoutException e) {
			tryAgainIfNeeded = false;
		} catch (ParserConfigurationException e) {
			LOG.error("ParserConfigurationException in parseLinkFeedFromUrl for url " + url, e);
		} catch (IOException e) {
			LOG.error("IOException in parseLinkFeedFromUrl for url " + url, e);
		} catch (SAXException e) {
			LOG.error("SAXException in parseLinkFeedFromUrl for url " + url, e);
		} catch (Exception e) {
			LOG.error("Exception in parseLinkFeedFromUrl for url " + url, e);
		} finally {
            if (get != null) {
                get.releaseConnection();
            }
		}
		// In case the new way of getting the bodyContent above fails, try it the old way.
		if (html.isEmpty() && tryAgainIfNeeded) {
			LOG.info("Could not parse RSS bodyContent initially, trying old method for URL: " + url);
			html = this.parseRssDocBodyFromUrlAlternative(url);
		}
		lf.setTitle(title.trim());
		lf.updateBodyText(html.trim());
		lf.updateRichBodyText(richHtml.trim());
    	return lf;
    }
	
	private String prepareHtmlForViewing(String html) {
		html = StringEscapeUtils.escapeHtml(html.trim());
		html = html.replaceAll("[ ]+\\n", "\n");
		html = html.replaceAll("\\n[ ]+", "\n");
		html = html.replaceAll("[\\n]+", "\n");
//		html = html.replaceAll("\\t+", "");
		html = html.replaceAll("\\n", " <BR/><BR/> ");
		html = this.handleUrls(html);
		return html;
	}
	
	private String getStringFromDocumentXpath(Document doc, String xpathStr, XPath xpath) throws Exception {
		String html = "";
		NodeList nodes = null;
		nodes = (NodeList) xpath.evaluate(xpathStr, doc, XPathConstants.NODESET);
		if (nodes.getLength() > 0) {
			Node node = nodes.item(0);
			html = node.getTextContent();
		}
		return html;
	}
	
	private String parseRssDocBodyFromUrlAlternative(String link) {
    	String html = "";
		try {
			String pageContent = this.getStringFromRssUrl(link);
	        if (pageContent.indexOf("bodyContent") > 0) {
	        	LOG.info("BodyContent Type\r\n");
	        	html = this.getCDataPage(pageContent.trim(), "/rss/channel/bodyContent");
	        } else {
	        	LOG.info("CDATA Type\r\n");
	        	html = this.getCDataPage(pageContent.trim(), "/rss/channel/description/text()");
	        }
	        if ("".equals(html.trim())) {
	        	html = this.getCDataPage(pageContent.trim(), "/rss/channel/item[1]/description/text()");
	        }
			html = StringEscapeUtils.escapeHtml(html.trim());
			html = html.replaceAll("\\n  \\n", "\n");
			html = html.replaceAll("\\n \\n", "\n");
			html = html.replaceAll("\\n\\n", "\n");
			html = html.replaceAll("\\n", " <BR/><BR/> ");
			html = this.handleUrls(html);
		} catch (Exception e) {
			LOG.error("Error processing page: " + link, e);
			html = "";
		}
        return html;
    }
	
	private String getCDataPage(String htmlPageAsString, String xpathStr) throws Exception {
		String html = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(htmlPageAsString)));
		XPathFactory fac = XPathFactory.newInstance();
		XPath xpath = fac.newXPath();
		NodeList nodes = null;
		nodes = (NodeList) xpath.evaluate(xpathStr, doc, XPathConstants.NODESET);
		if (nodes.getLength() > 0) {
			Node node = nodes.item(0);
			html = node.getTextContent();
		}
		return html;
	}
	
	private String handleUrls(String text) {
		boolean b;
		String s;
		StringBuffer sb = new StringBuffer();
		 //this.getCacheService().findConfigParamValueByName(Constants.EVENTS_URL_MATCHER_REGEX);
		String regex = configParamService.findValueByName(IU_NEWS_URL_MATCHER_REGEX);
		if (regex == null || regex.length() < 1) {
			// Below, without the extra slashes: (http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?
			regex = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
//			regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		}
		//this.getCacheService().findConfigParamValueByName(Constants.EVENTS_URL_MATCHER_FORMAT);
		String stringFormat = configParamService.findValueByName(IU_NEWS_URL_MATCHER_FORMAT); 
		if (stringFormat == null || stringFormat.length() < 1) {
			stringFormat = "<a target='_blank' href='%s'>%s</a>";
		}
		Formatter fmt = new Formatter(sb, Locale.US);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		b = true;
		while (b == true) {
			b = m.find();
			if (b) {
				s = m.group();
				m.appendReplacement(sb, "");
				String newStr = StringEscapeUtils.escapeHtml(s);
				fmt.format(stringFormat, newStr, newStr);
			}
		}
		m.appendTail(sb);
		return sb.toString();
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
            String param = configParamService.findValueByName(name);
            if (param != null && !"".equals(param)) {
                timeout = new Integer(param) * 1000;
            }
        } catch (Exception e) {
            // Use default
        }
        return timeout;
    }

	public void deleteRss(Rss rss) {
	  	Rss rssToDelete = this.getRssFromDatabase(rss.getRssMaintId());
	  	if (rssToDelete != null && rssToDelete.getRssId() != null) {
	  		rssDao.deleteRssById(rssToDelete.getRssId());	
	  	}
	}
	
	public void saveRss(Rss rss) {
        rssDao.saveRss(rss);
    }

    public void updateRss(Rss rss) {
        saveRss(rss);
    }
    
    public Rss findRssById(Long rssId) {
    	Rss rss = null;
		try {
			rss = rssDao.findRssById(rssId);
		} catch (NoResultException e) {
			// Not a problem.
		} catch (Exception e) {
			LOG.error("Error loading RSS from database: ", e);
		}
        return rss;
    }
}
