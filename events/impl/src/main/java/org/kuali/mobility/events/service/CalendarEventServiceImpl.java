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

package org.kuali.mobility.events.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.common.signature.SignatureSecret;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.CoreOAuthConsumerSupport;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.OAuthRequestFailedException;
import org.springframework.security.oauth.consumer.OAuthRestTemplate;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.net.DefaultOAuthURLStreamHandlerFactory;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

public class CalendarEventServiceImpl implements CalendarEventService {

    private String eventsURL;

    private String eventURL;

    private String SERVER_URL = "https://test.uisapp2.iu.edu/ccl-unt";

    private String SERVER_URL_OAUTH_CALLBACK = "oob";
    
    private String SERVER_URL_OAUTH_REQUEST = SERVER_URL + "/oauth/request_token";

    private String SERVER_URL_OAUTH_AUTHZ = SERVER_URL + "/oauth/authorize";

    private String SERVER_URL_OAUTH_ACCESS = SERVER_URL + "/oauth/access_token";

    private String CONSUMER_KEY = "mobileOAuthConsumerKey";

    private String CONSUMER_SECRET = "hello";

    private String SIGNATURE_METHOD = "HMAC-SHA1";

    private String RESOURCE_ID = "ONS_ID";

    private OAuthRestTemplate eventRestTemplate;

    public Map<String, String> getEventsTake2(final String username) throws Exception {
        CoreOAuthConsumerSupport localConsumerSupport = new CoreOAuthConsumerSupport();
        localConsumerSupport.setStreamHandlerFactory(new DefaultOAuthURLStreamHandlerFactory());
        localConsumerSupport.setProtectedResourceDetailsService(new ProtectedResourceDetailsService() {

            public ProtectedResourceDetails loadProtectedResourceDetailsById(String id) throws IllegalArgumentException {
                SignatureSecret secret = new SharedConsumerSecret(CONSUMER_SECRET);

                BaseProtectedResourceDetails result = new BaseProtectedResourceDetails();
                result.setConsumerKey(CONSUMER_KEY);
                result.setSharedSecret(secret);
                result.setSignatureMethod(SIGNATURE_METHOD);
                result.setUse10a(true);
                result.setRequestTokenURL(SERVER_URL_OAUTH_REQUEST);
                result.setAccessTokenURL(SERVER_URL_OAUTH_ACCESS);
                result.setUserAuthorizationURL(SERVER_URL_OAUTH_AUTHZ);
                Map<String, String> requestHeaders = new HashMap<String, String>();
                requestHeaders.put("username", username);
                requestHeaders.put("Accept", "application/json");
                result.setAdditionalRequestHeaders(requestHeaders);
                return result;
            }
        });

        OAuthConsumerSupport consumerSupport = localConsumerSupport;

        OAuthConsumerToken token = getRequestToken(consumerSupport);
        String requestTokenVerifier = authorizeRequestToken(token);
        OAuthConsumerToken accessToken = getAccessToken(token, requestTokenVerifier, consumerSupport);
        String result = getProtectedResource(eventsURL, consumerSupport, accessToken);
//        String result = getProtectedResource(eventsURL + "?username=" + username, consumerSupport, accessToken);
        Map<String, String> events = new HashMap<String, String>();
        events.put("1", result);
        return events;
    }

    public Map<String, String> getEventTake2(String id, String username) throws Exception {
        CoreOAuthConsumerSupport localConsumerSupport = new CoreOAuthConsumerSupport();
        localConsumerSupport.setStreamHandlerFactory(new DefaultOAuthURLStreamHandlerFactory());
        localConsumerSupport.setProtectedResourceDetailsService(new ProtectedResourceDetailsService() {

            public ProtectedResourceDetails loadProtectedResourceDetailsById(String id) throws IllegalArgumentException {
                SignatureSecret secret = new SharedConsumerSecret(CONSUMER_SECRET);

                BaseProtectedResourceDetails result = new BaseProtectedResourceDetails();
                result.setConsumerKey(CONSUMER_KEY);

                result.setSharedSecret(secret);
                result.setSignatureMethod(SIGNATURE_METHOD);
                result.setUse10a(false);
                result.setRequestTokenURL(SERVER_URL_OAUTH_REQUEST);
                result.setAccessTokenURL(SERVER_URL_OAUTH_ACCESS);
                result.setUserAuthorizationURL(SERVER_URL_OAUTH_AUTHZ);
                return result;
            }
        });

        OAuthConsumerSupport consumerSupport = localConsumerSupport;

        OAuthConsumerToken token = getRequestToken(consumerSupport);
        String requestTokenVerifier = authorizeRequestToken(token);
        OAuthConsumerToken accessToken = getAccessToken(token, requestTokenVerifier, consumerSupport);
        String result = getProtectedResource(eventURL + "/" + id + "?username=" + username, consumerSupport, accessToken);
        Map<String, String> events = new HashMap<String, String>();
        events.put("1", result);
        return events;
    }

    public Map<String, String> getEvents(String username) {
        Map<String, String> events = new HashMap<String, String>();
        byte[] foundEvents = getEventRestTemplate().getForObject(URI.create(String.format(getEventsURL())), byte[].class);
        events.put("1", new String(foundEvents));

        return events;
    }

    public String getEvent(String id) {
        byte[] event = getEventRestTemplate().getForObject(URI.create(String.format(getEventURL(), id)), byte[].class);
        String foundEvent = new String(event);
        return foundEvent;
    }

    private OAuthConsumerToken getRequestToken(OAuthConsumerSupport consumerSupport) {
        return consumerSupport.getUnauthorizedRequestToken(RESOURCE_ID, SERVER_URL_OAUTH_CALLBACK);
    }

    private String authorizeRequestToken(OAuthConsumerToken requestToken) throws HttpException, IOException {
        int resultCode = 0;
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        HttpClient httpClient = new HttpClient();

        PostMethod authorizeMethod = new PostMethod(SERVER_URL_OAUTH_AUTHZ);
        authorizeMethod.addParameter("requestToken", requestToken.getValue());
        authorizeMethod.addParameter("authorize", "Authorize");
        authorizeMethod.setFollowRedirects(false);

        resultCode = httpClient.executeMethod(authorizeMethod);
        String body = authorizeMethod.getResponseBodyAsString();
        String redirectURL = authorizeMethod.getResponseHeader("Location").getValue();
        authorizeMethod.releaseConnection();
        if (redirectURL != null && redirectURL.indexOf("oauth_verifier") > -1) {
            return redirectURL.substring(redirectURL.indexOf("oauth_verifier") + 15);
        }
        return "";
    }

    private OAuthConsumerToken getAccessToken(OAuthConsumerToken requestToken, String requestTokenVerifier, OAuthConsumerSupport consumerSupport) {
        return consumerSupport.getAccessToken(requestToken, requestTokenVerifier);
    }

    private String getProtectedResource(String url, OAuthConsumerSupport consumerSupport, OAuthConsumerToken accessToken) throws OAuthRequestFailedException, IOException {
        InputStream is = consumerSupport.readProtectedResource(new URL(url), accessToken, "GET");
        if (is != null) {
            String output = "";
            try {
                BufferedReader myInput = new BufferedReader(new InputStreamReader(is));
                String currentLine = null;
                while ((currentLine = myInput.readLine()) != null) {
                    output = output + currentLine;
                }
            } finally {
                is.close();
            }
            return output;
        }
        return "";
    }

    public String getEventsURL() {
        return eventsURL;
    }

    public void setEventsURL(String eventsURL) {
        this.eventsURL = eventsURL;
    }

    public String getEventURL() {
        return eventURL;
    }

    public void setEventURL(String eventURL) {
        this.eventURL = eventURL;
    }

    public OAuthRestTemplate getEventRestTemplate() {
        return eventRestTemplate;
    }

    public void setEventRestTemplate(OAuthRestTemplate eventRestTemplate) {
        this.eventRestTemplate = eventRestTemplate;
    }
    
}
