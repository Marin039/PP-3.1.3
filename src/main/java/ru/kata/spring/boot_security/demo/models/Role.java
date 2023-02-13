package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table (name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @Column (name = "role_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "role")
    private String rolename;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set <User> users;

    public Role() {
    }

    public Role(String rolename) {
        this.rolename = rolename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRoleName(String rolename) {
        this.rolename = rolename;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", rolename='" + rolename + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        return getRolename();
    }
}
