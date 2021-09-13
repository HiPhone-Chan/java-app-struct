package com.chf.app.domain.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.chf.app.domain.Navigation;
import com.chf.app.domain.StaffRole;

@Embeddable
public class NavigationRoleId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Navigation navigation;

    @ManyToOne
    private StaffRole role;

    public NavigationRoleId() {
    }

    public NavigationRoleId(Navigation navigation, StaffRole role) {
        this.navigation = navigation;
        this.role = role;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((navigation == null) ? 0 : navigation.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
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
        NavigationRoleId other = (NavigationRoleId) obj;
        if (navigation == null) {
            if (other.navigation != null)
                return false;
        } else if (!navigation.equals(other.navigation))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

}
