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

package org.kuali.mobility.knowledgebase.entity;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseSearchResultContainer {

	private List<KnowledgeBaseSearchResult> results;
	
	private String query;
	private String version;
	private String ignored;
	private String advanced;
	private int start;
	private int count;
	private int indexLastInView;
	private int numberOfResults;
	private int maxResults;

	public KnowledgeBaseSearchResultContainer() {
		this.results = new ArrayList<KnowledgeBaseSearchResult>();
		this.maxResults = 300;
	}
	
	/*
	 * TODO: Paging should be seperated out into another object for reuse.
	 */
	
	public boolean isPreviousPageAvailable() {
		return getPreviousPageEndIndex() > 0;
	}

	public boolean isNextPageAvailable() {
		int highestIndex = numberOfResults;
		if (maxResults < numberOfResults) {
			highestIndex = maxResults;
		}
		return getNextPageStartIndex() < highestIndex;
	}
	
	public int getPreviousPageStartIndex() {
		return start - count;
	}
	
	public int getPreviousPageEndIndex() {
		return start - 1;
	}
	
	public int getNextPageStartIndex() {
		return start + count;
	}
	
	public int getNextPageEndIndex() {
		return start + (2 * count) - 1;
	}
	
	public List<KnowledgeBaseSearchResult> getResults() {
		return results;
	}

	public void setResults(List<KnowledgeBaseSearchResult> results) {
		this.results = results;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIgnored() {
		return ignored;
	}

	public void setIgnored(String ignored) {
		this.ignored = ignored;
	}

	public String getAdvanced() {
		return advanced;
	}

	public void setAdvanced(String advanced) {
		this.advanced = advanced;
	}
	
	public int getNumberOfResults() {
		return numberOfResults;
	}

	public void setNumberOfResults(int numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getIndexLastInView() {
		return indexLastInView;
	}

	public void setIndexLastInView(int indexLastInView) {
		this.indexLastInView = indexLastInView;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
