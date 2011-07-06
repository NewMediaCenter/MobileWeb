package org.kuali.mobility.news.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name="RSS_T")
public class Rss implements Serializable {

	private static final long serialVersionUID = 9179814979533742706L;

    @Id
    @SequenceGenerator(name="rss_sequence", sequenceName="SEQ_RSS_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rss_sequence")
    @Column(name="RSS_ID")
    private Long rssId;

    @Column(name="RSS_TITLE")
	private String title;

    @Column(name="RSS_URL")
	private String url;

    @Column(name="LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

    @Column(name="LINK")
	private String link;

    @Column(name="IMG_LOC")
	private String imageLocation;

    @Column(name="FRM_URL")
	private String formLink;

    @Column(name="FRM_DESC")
	private String formDescription;

    @Column(name="FRM_TITLE")
	private String formTitle;

    @Column(name="FRM_NM")
	private String formName;
    
    @Column(name="RSS_MAINT_ID")
    private Long rssMaintId;
    
    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
	
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, mappedBy="rss")
	private List<RssItem> rssItems;
	
    @Transient
	private boolean update;
    
    @Transient
	private boolean delete;

	@Transient
	private boolean putInCache;

	public Rss() {
		this.rssItems = new ArrayList<RssItem>();
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public Long getRssId() {
		return rssId;
	}

	public void setRssId(Long rssId) {
		this.rssId = rssId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFormLink() {
		return formLink;
	}

	public void setFormLink(String formLink) {
		this.formLink = formLink;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public List<RssItem> getRssItems() {
		return rssItems;
	}

	public void setRssItems(List<RssItem> rssItems) {
		this.rssItems = rssItems;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isPutInCache() {
		return putInCache;
	}

	public void setPutInCache(boolean putInCache) {
		this.putInCache = putInCache;
	}

    public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public Long getRssMaintId() {
		return rssMaintId;
	}

	public void setRssMaintId(Long rssMaintId) {
		this.rssMaintId = rssMaintId;
	}
	
}
