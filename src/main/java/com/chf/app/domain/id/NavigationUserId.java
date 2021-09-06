package com.chf.app.domain.id;

import javax.persistence.Embeddable;

import com.chf.app.domain.Navigation;
import com.chf.app.domain.User;

@Embeddable
public class NavigationUserId {

    private Navigation navigation;

    private User user;

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
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
        result = prime * result + ((navigation == null) ? 0 : navigation.hashCode());
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
        NavigationUserId other = (NavigationUserId) obj;
        if (navigation == null) {
            if (other.navigation != null)
                return false;
        } else if (!navigation.equals(other.navigation))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

}
