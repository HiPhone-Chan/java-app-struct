package com.chf.app.domain.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.chf.app.domain.ApiInfo;
import com.chf.app.domain.StaffRole;

@Embeddable
public class RoleApiId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private StaffRole role;

    @ManyToOne
    private ApiInfo apiInfo;

    public RoleApiId() {
    }

    public RoleApiId(StaffRole role, ApiInfo apiInfo) {
        super();
        this.role = role;
        this.apiInfo = apiInfo;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apiInfo == null) ? 0 : apiInfo.hashCode());
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
        RoleApiId other = (RoleApiId) obj;
        if (apiInfo == null) {
            if (other.apiInfo != null)
                return false;
        } else if (!apiInfo.equals(other.apiInfo))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

}
