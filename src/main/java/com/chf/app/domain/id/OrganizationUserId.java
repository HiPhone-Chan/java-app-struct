package com.chf.app.domain.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.chf.app.domain.Organization;
import com.chf.app.domain.User;

// 组织用户关系
@Embeddable
public class OrganizationUserId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Organization Organization;

    @ManyToOne
    private User user;

    public Organization getOrganization() {
        return Organization;
    }

    public void setOrganization(Organization organization) {
        Organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Organization == null) ? 0 : Organization.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        OrganizationUserId other = (OrganizationUserId) obj;
        if (Organization == null) {
            if (other.Organization != null)
                return false;
        } else if (!Organization.equals(other.Organization))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

}
