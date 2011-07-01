package org.kuali.mobility.news.service;

import org.kuali.mobility.news.entity.Rss;

public interface RssService {

	public Rss fetch(String url) throws Exception;
}
