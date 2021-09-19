package com.chf.app.domain.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.chf.app.domain.ApiInfo;
import com.chf.app.domain.Navigation;

@Embeddable
public class NavigationApiId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Navigation navigation;

    @ManyToOne
    private ApiInfo apiInfo;

    public NavigationApiId() {
    }

    public NavigationApiId(Navigation navigation, ApiInfo apiInfo) {
        super();
        this.navigation = navigation;
        this.apiInfo = apiInfo;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apiInfo == null) ? 0 : apiInfo.hashCode());
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
        NavigationApiId other = (NavigationApiId) obj;
        if (apiInfo == null) {
            if (other.apiInfo != null)
                return false;
        } else if (!apiInfo.equals(other.apiInfo))
            return false;
        if (navigation == null) {
            if (other.navigation != null)
                return false;
        } else if (!navigation.equals(other.navigation))
            return false;
        return true;
    }

}
