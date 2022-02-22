package com.example.ui.security.data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Integer id;

    @Column(name="user_name")
    private String name;

    @Column(name="user_passwd")
    private String password;

    @Column(name="user_email")
    private String email;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(
            name="roles",
            joinColumns = @JoinColumn(name="user_id")
    )
    @Column(name="user_role")
    private List<String> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(String... roles) {
        this.roles = Arrays.asList(roles);
    }
}
