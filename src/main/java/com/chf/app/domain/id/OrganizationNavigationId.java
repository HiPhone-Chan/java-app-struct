package com.chf.app.domain.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.chf.app.domain.Navigation;
import com.chf.app.domain.Organization;

// 组织导航关系
@Embeddable
public class OrganizationNavigationId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Organization Organization;

    @ManyToOne
    private Navigation navigation;

    // 页面使用到的api
    @Column(name = "apis", length = 511)
    private String apis;

    public Organization getOrganization() {
        return Organization;
    }

    public void setOrganization(Organization organization) {
        Organization = organization;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public String getApis() {
        return apis;
    }

    public void setApis(String apis) {
        this.apis = apis;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Organization == null) ? 0 : Organization.hashCode());
        result = prime * result + ((navigation == null) ? 0 : navigation.hashCode());
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
        OrganizationNavigationId other = (OrganizationNavigationId) obj;
        if (Organization == null) {
            if (other.Organization != null)
                return false;
        } else if (!Organization.equals(other.Organization))
            return false;
        if (navigation == null) {
            if (other.navigation != null)
                return false;
        } else if (!navigation.equals(other.navigation))
            return false;
        return true;
    }

}
