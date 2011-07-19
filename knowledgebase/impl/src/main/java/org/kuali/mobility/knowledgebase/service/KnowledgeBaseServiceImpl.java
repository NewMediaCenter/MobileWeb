/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.mobility.knowledgebase.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.knowledgebase.entity.KnowledgeBaseSearchResult;
import org.kuali.mobility.knowledgebase.entity.KnowledgeBaseSearchResultContainer;
import org.kuali.mobility.xsl.dao.XslDao;
import org.kuali.mobility.xsl.entity.Xsl;
import org.springframework.beans.factory.annotation.Autowired;

import flexjson.JSONSerializer;

public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KnowledgeBaseServiceImpl.class);
	
    @Autowired
    private XslDao xslDao;
    public void setXslDao(XslDao xslDao) {
        this.xslDao = xslDao;
    }
    
    @Autowired
    private ConfigParamService configParamService;
    public void setConfigParamService(ConfigParamService configParamService) {
    	this.configParamService = configParamService;
    }
	
	private String username;
	private String password;

	private static final String PARAM_KB_URL_DOCUMENT = "KB.Url.Document";		// http://remote.kb.iu.edu/REST/v0.2/document/
	private static final String PARAM_KB_URL_SEARCH = "KB.Url.Search";			//
	
    public String toJson(KnowledgeBaseSearchResultContainer container) {
        return new JSONSerializer().exclude("*.class").include("results").serialize(container);
    }
	
	private Templates getXslTemplates(String templatesCode) throws Exception {
		Xsl xsl = xslDao.findXslByCode(templatesCode);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Templates templates = transformerFactory.newTemplates(new StreamSource(new ByteArrayInputStream(xsl.getValue().getBytes())));
//		Templates templates = transformerFactory.newTemplates(new StreamSource(new ByteArrayInputStream("test".getBytes())));
		return templates;
	}
	
	public KnowledgeBaseSearchResultContainer searchKnowledgeBase(String query, int index, int size) {
		KnowledgeBaseSearchResultContainer container = null;
		try {
			// We could probably just return a document for this and use an XSL to transform it in the mobile service implementation.
//			String url = this.getCacheService().findConfigParamValueByName(PARAM_KB_URL_SEARCH);
//			String url = "http://remote.kb.iu.edu/REST/v0.2/document/search//";
			String url = this.configParamService.findValueByName(PARAM_KB_URL_SEARCH);
			String content = "query=" + URLEncoder.encode(query, "UTF-8");
			if (index > 0) {
				content = content + "&start=" + index;
			}
			if (size > 0) {
				content = content + "&size=" + size;
			}
			RequestEntity requestEntity = new StringRequestEntity(content, null, "UTF-8");
			
//			Document doc = this.callKnowledgeBase(url, requestEntity, true);
			String searchXml = this.callKnowledgeBase(url, requestEntity, true);
			
			SAXBuilder builder = new SAXBuilder();
			StringReader reader = new StringReader(searchXml);
			Document doc = builder.build(reader);

			container = convertSearchDocument(doc);
			container.setCount(size);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		} catch (JDOMException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return container;
	}


	private KnowledgeBaseSearchResultContainer convertSearchDocument(Document doc) {
		KnowledgeBaseSearchResultContainer container = new KnowledgeBaseSearchResultContainer();
		try {
			if (doc != null) {
				Element root = doc.getRootElement();
				String numResultsStr = root.getChildText("numResults");
				int numResults = 0;
				try {
					numResults = Integer.parseInt(numResultsStr);
					if (numResults > 300) {
						numResults = 300;
					}
					container.setNumberOfResults(numResults);
				} catch (NumberFormatException e) {}
				
				String startStr = root.getChildText("start");
				int start = 0;
				try {
					start = Integer.parseInt(startStr);
					container.setStart(start);
					container.setIndexLastInView(start);
				} catch (NumberFormatException e) {}
				
				if (numResults > 0) {
					Element results = root.getChild("results");
					List resultList = results.getChildren("result");
					boolean firstResult = true;
					int count = 0;
					for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
						Element result = (Element) iterator.next();
						String documentId = processChildText(result, "docid");
						String title = processChildText(result, "title");
						KnowledgeBaseSearchResult searchResult = new KnowledgeBaseSearchResult();
						searchResult.setDocumentId(documentId);
						searchResult.setTitle(title);
						container.getResults().add(searchResult);
						// Index of the last item in view should not increment for the first result.
						if (firstResult) {
							firstResult = false;
						} else {
							container.setIndexLastInView(container.getIndexLastInView() + 1);	
						}
						count++;
					}
				}
				// TESTING
//			String xml = new XMLOutputter().outputString(doc);
//			LOG.info(xml);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return container;
	}
	
	private String processChildText(Element result, String name) {
		String output = result.getChildText(name);
		output = this.escapeInvalidEntities(output);
		return output;
	}

	public String getKnowledgeBaseDocument(String documentId) {
		String docStr = "";
		try {
//			String urlFormat = "http://remote.kb.iu.edu/REST/v0.2/document/%s";
//			String urlFormat = this.getCacheService().findConfigParamValueByName(PARAM_KB_URL_DOCUMENT);
			String urlFormat = this.configParamService.findValueByName(PARAM_KB_URL_DOCUMENT);
			String args[] = { documentId };
			String url = String.format(Locale.US, urlFormat, (Object[]) args); //"http://remote.kb.iu.edu/REST/v0.2/document/" + documentId;
			docStr = this.callKnowledgeBase(url, null, false);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return docStr;
	}

	public String getConvertedKnowledgeBaseDocument(String documentId, String templatesCode, Map<String, Object> transformerParameters) {
		String xml = this.getKnowledgeBaseDocument(documentId);
		String output = "";
		try {
			Templates templates = this.getXslTemplates(templatesCode);
//			Templates templates = this.cacheService.findCachedXSLTemplates(templatesCode);
			Transformer transformer = templates.newTransformer();
			for (String key :transformerParameters.keySet()) {
				transformer.setParameter(key, transformerParameters.get(key));
			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        transformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes())), new StreamResult(byteArrayOutputStream));
	        output = byteArrayOutputStream.toString();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return output;
	}

	private String callKnowledgeBase(String url, RequestEntity requestEntity, boolean isPost) throws Exception {
		String output = null;
//		Document doc = null;
//		SAXBuilder builder = new SAXBuilder();
//		BufferedReader in = null;
		
		HttpClient client = null;
		client = new HttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(this.username, this.password);
		client.getState().setCredentials(new AuthScope("remote.kb.iu.edu", 80, AuthScope.ANY_REALM), defaultcreds);
		HttpMethodBase method = null;
		if (isPost) {
			method = preparePostMethod(url, requestEntity);
		} else {
			method = new GetMethod(url);
		}
		method.setDoAuthentication(true);
		
//		int timeout = getSocketTimeout(Constants.RSS_SOCKET_TIMEOUT_SECONDS, Constants.RSS_SOCKET_DEFAULT_TIMEOUT);
		int timeout = getSocketTimeout("blah", 5000);
		client.getParams().setParameter(HttpClientParams.SO_TIMEOUT, new Integer(timeout));
		client.getParams().setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, new Long(timeout));
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, new Integer(timeout));
		try {
            int status = client.executeMethod(method);
//            System.out.println(status + "\n" + get.getResponseBodyAsString());
//            in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
//            doc = builder.build(in);
            output = this.inputStreamToString(method.getResponseBodyAsStream());
        } finally {
        	method.releaseConnection();
        }
		
		return output;
	}
	
	private HttpMethodBase preparePostMethod(String url, RequestEntity requestEntity) {
		PostMethod method = new PostMethod(url);
		if (requestEntity != null) {
			method.setRequestEntity(requestEntity);	
		}
		return method;
	}
	
	private String inputStreamToString(InputStream in) {
		StringBuffer sb = new StringBuffer();
		try {
		Scanner scanner = new Scanner(in);
			try {
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine() + "\r\n");
				}
			} finally {
				scanner.close();
			}
		} catch (Exception e) {
//			LOG.error("Error retrieving XSL: ", e);
		}
		return sb.toString();
	}
	
	/*
	 * Helper methods
	 */
	
	private String escapeInvalidEntities(String xml) {
		try {
			String replacements[] = this.getReplacementTokens().split(",");
			// LOG.info("start getting rssDescription tokens:" + url);
			for (String replacement : replacements) {
				String[] tokens = replacement.split("=");
				xml = xml.replace(tokens[0].trim(), tokens[1].trim());
			}
			// LOG.info("end getting rssDescription tokens:" + url);
		} catch (Exception e) {
//			LOG.error("Problem escaping invalid entities for XML: " + xml, e);
		}
		return xml;
	}
	
	private String getReplacementTokens() {
		String replacementTokens = "";
		/*
		String replacementTokens = ServiceLocator.getCacheService().findConfigParamValueByName(Constants.DATA_XML_REPLACEMENT_TOKENS);
		if (replacementTokens == null) {
			replacementTokens = Constants.EMPTY_STRING;
		}
		*/
		return replacementTokens;
	}
	
    private Integer getSocketTimeout(String name, int defaultTimeoutInMillis) {
        int timeout = defaultTimeoutInMillis;
        /*
        try {
            String param = cacheService.findConfigParamValueByName(name);
            if (param != null && !"".equals(param)) {
                timeout = new Integer(param) * 1000;
            }
        } catch (Exception e) {
            // Use default
        }
        */
        return timeout;
    }
    
    /*
     * Services
     */

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
