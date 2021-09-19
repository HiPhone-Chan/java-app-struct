package com.chf.app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// 菜单，导航
@Entity
@Table(name = "navigation", indexes = { @Index(columnList = "path"), @Index(columnList = "region") })
public class Navigation {

    @Id
    private String id;

    @Column(name = "title", length = 63)
    private String title;

    @Column(name = "path", length = 511)
    private String path;

    @Column(name = "icon", length = 511)
    private String icon;
    // 使用区域
    @Column(name = "region", length = 31)
    private String region;
    // 优先级
    @Column(name = "priority")
    private Integer priority;

    @ManyToOne
    private Navigation parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Navigation getParent() {
        return parent;
    }

    public void setParent(Navigation parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Navigation other = (Navigation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
