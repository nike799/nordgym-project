package nordgym.domain.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

@Entity(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    private String authority;

    public Role() {
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
