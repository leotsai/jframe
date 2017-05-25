package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * Created by leo on 2017-05-25.
 */
@Entity(name = "roles")
public class Role extends EntityBase {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = true, length = 200)
    private String description;

    //--------------------------------------------------

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "role")
    private Collection<UserRoleRL> userRoleRLs;

    //--------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<UserRoleRL> getUserRoleRLs() {
        return userRoleRLs;
    }

    public void setUserRoleRLs(Collection<UserRoleRL> userRoleRLs) {
        this.userRoleRLs = userRoleRLs;
    }
}
