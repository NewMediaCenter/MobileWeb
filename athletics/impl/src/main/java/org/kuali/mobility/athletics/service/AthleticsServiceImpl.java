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

package org.kuali.mobility.athletics.service;

import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.kuali.mobility.athletics.entity.Athletics;

import flexjson.JSONDeserializer;

public class AthleticsServiceImpl implements AthleticsService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AthleticsServiceImpl.class);
	
	private int socketTimeout = 10000;
	
	private int connectionManagerTimeout = 10000;

	private int connectionTimeout = 10000;

	private String athleticsURL;

	public Athletics retrieveAthletics() throws Exception {
		GetMethod get = new GetMethod(athleticsURL + "sports.do?version=2");
		String json = IOUtils.toString(getInputStreamFromGetMethod(get), "UTF-8");
		return new JSONDeserializer<Athletics>().deserialize(json, Athletics.class);
	}

	private InputStream getInputStreamFromGetMethod(GetMethod get) throws Exception {
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpClientParams.SO_TIMEOUT, new Integer(socketTimeout));
		httpClient.getParams().setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, new Long(connectionManagerTimeout));
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, new Integer(connectionTimeout));
		int status = httpClient.executeMethod(get);
		if (status == HttpStatus.SC_OK) {
			return get.getResponseBodyAsStream();
		}
		return null;
	}

	public String getAthleticsURL() {
		return athleticsURL;
	}

	public void setAthleticsURL(String athleticsURL) {
		this.athleticsURL = athleticsURL;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionManagerTimeout() {
		return connectionManagerTimeout;
	}

	public void setConnectionManagerTimeout(int connectionManagerTimeout) {
		this.connectionManagerTimeout = connectionManagerTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

}
