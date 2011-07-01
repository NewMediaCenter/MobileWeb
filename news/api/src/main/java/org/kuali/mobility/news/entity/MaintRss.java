package org.kuali.mobility.news.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity(name="MaintRss")
@Table(name="RSS_MAINT_T")
public class MaintRss implements Comparable<MaintRss>, Serializable {

    private static final long serialVersionUID = 8753764116073085733L;

    @Id
    @SequenceGenerator(name="rss_maint_sequence", sequenceName="SEQ_RSS_MAINT_T", initialValue=1000, allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rss_maint_sequence")
    @Column(name="RSS_ID")
    private Long rssId;

    @Column(name="RSS_SHORT_CODE")
	private String shortCode;

    @Column(name="RSS_NAME")
    private String displayName;

    @Column(name="RSS_URL")
	private String url;
    
    @Column(name="RSS_ORDER")
    private int order;
    
    @Column(name="CAMPUS")
    private String campus;

    @Column(name="RSS_TYPE")
    private String type;

    @Column(name="INTRVL_MIN")
    private Long intervalMinute;
    
    @Column(name="ACTIVE")
    private boolean active;
    
    @Version
    @Column(name="VER_NBR")
    protected Long versionNumber;
	
	public MaintRss() {
	}

	public Long getRssId() {
		return rssId;
	}

	public void setRssId(Long rssId) {
		this.rssId = rssId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
	public MaintRss copy() {
		MaintRss maintRss = this;
		MaintRss maintRssCopy = new MaintRss();
		if (maintRss.getCampus() != null) {
			maintRssCopy.setCampus(new String(maintRss.getCampus()));
		}
		if (maintRss.getDisplayName() != null) {
			maintRssCopy.setDisplayName(new String(maintRss.getDisplayName()));
		}
		if (maintRss.getRssId() != null) {
			maintRssCopy.setRssId(new Long(maintRss.getRssId()));
		}
		if (maintRss.getShortCode() != null) {
			maintRssCopy.setShortCode(new String(maintRss.getShortCode()));
		}
		if (maintRss.getType() != null) {
			maintRssCopy.setType(new String(maintRss.getType()));
		}
		if (maintRss.getUrl() != null) {
			maintRssCopy.setUrl(new String(maintRss.getUrl()));
		}
		if (maintRss.getVersionNumber() != null) {
			maintRssCopy.setVersionNumber(new Long(maintRss.getVersionNumber()));
		}
		if (maintRss.getIntervalMinute() != null) {
			maintRssCopy.setIntervalMinute(new Long(maintRss.getIntervalMinute()));
		}
		maintRssCopy.setActive(maintRss.isActive());
		maintRssCopy.setOrder(maintRss.getOrder());
		return maintRssCopy;
	}
	
    public int compareTo(MaintRss that) {
        if (this == null || that == null) {
            return -1;
        }
        if (this.order == that.order) {
        	return 0;
        }
        if (this.order > that.order) {
        	return 1;
        }
        return -1;
    }

	public Long getIntervalMinute() {
		return intervalMinute;
	}

	public void setIntervalMinute(Long intervalMinute) {
		this.intervalMinute = intervalMinute;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
