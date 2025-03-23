package com.Harum.Harum.Models;


import com.Harum.Harum.Enums.RoleTypes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Roles {

    @Id
    private String id;
    private RoleTypes roleName;

    public Roles() {}

    public Roles(RoleTypes roleName) {
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleTypes getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleTypes roleName) {
        this.roleName = roleName;
    }
}