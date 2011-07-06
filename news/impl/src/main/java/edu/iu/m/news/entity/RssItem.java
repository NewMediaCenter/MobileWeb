package edu.iu.m.news.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name="RSS_ITM_T")
public class RssItem implements Serializable, Comparable<RssItem> {

	private static final long serialVersionUID = 5876069955013500644L;

    @Id
    @SequenceGenerator(name="rss_item_sequence", sequenceName="SEQ_RSS_ITM_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rss_item_sequence")
    @Column(name="RSS_ITM_ID")
	private Long rssItemId;

    @Column(name="TITLE")
    private String title;

    @Column(name="DESCRIPTION")
	private String description;

    @Column(name="LINK")
	private String link;

    @Column(name="ORDR")
	private Long order;

    @Column(name="PBLSH_DT")
	private Timestamp publishDate;
    
    @Column(name="ENC_URL")
    private String enclosureUrl;
    
    @Column(name="ENC_LENGTH")
    private Long enclosureLength;
    
    @Column(name="ENC_TYPE")
    private String enclosureType;
    
    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
    	name="RSS_ID", nullable=false
    )
    private Rss rss;
    
    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
    
    @Transient
	private List<RssCategory> categories;
	
	
    public int compareTo(RssItem that) {
        if (getOrder() != null && that.getOrder() != null) {
            return getOrder().compareTo(that.getOrder());
        }
        return -1;
    }
    
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String getLinkUrlEncoded() {
		String s = "";
		try {
			s = URLEncoder.encode(link, "UTF-8");	
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
		}
		return s;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Rss getRss() {
		return rss;
	}

	public void setRss(Rss rss) {
		this.rss = rss;
	}

//	public Long getRssId() {
//		return rssId;
//	}
//
//	public void setRssId(Long rssId) {
//		this.rssId = rssId;
//	}

	public Long getRssItemId() {
		return rssItemId;
	}

	public void setRssItemId(Long rssItemId) {
		this.rssItemId = rssItemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<RssCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<RssCategory> categories) {
		this.categories = categories;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}
	
	public String getPublishDateFormatted() {
		if (publishDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a z");
			String publishDate = sdf.format(new Date(this.getPublishDate().getTime()));
			return publishDate;			
		} 
		return "";
	}

	public String getEnclosureUrl() {
		return enclosureUrl;
	}

	public void setEnclosureUrl(String enclosureUrl) {
		this.enclosureUrl = enclosureUrl;
	}

	public Long getEnclosureLength() {
		return enclosureLength;
	}

	public void setEnclosureLength(Long enclosureLength) {
		this.enclosureLength = enclosureLength;
	}

	public String getEnclosureType() {
		return enclosureType;
	}

	public void setEnclosureType(String enclosureType) {
		this.enclosureType = enclosureType;
	}

}
