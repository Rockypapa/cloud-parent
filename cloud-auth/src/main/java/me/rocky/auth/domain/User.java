package me.rocky.auth.domain;

import cn.hutool.core.collection.CollectionUtil;
import me.rocky.model.AuthMemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/2 11:12
 * @log
 */
public class User implements UserDetails {

    private static final long serialVersionUID = 6141426646383491433L;
    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    private String clientId;

    private Collection<SimpleGrantedAuthority> authorities;

    public User() {
        this.enabled = false;
    }

    public User(AuthMemberDto dto){
        this.setId(dto.getId());
        this.setUsername(dto.getUsername());
        this.setPassword(dto.getPassword());
        this.enabled = dto.getStatus();
        this.setClientId(dto.getClientId());
        if (CollectionUtil.isNotEmpty(dto.getRoles())){
            authorities = new ArrayList<>();
            dto.getRoles().forEach(e-> authorities.add(new SimpleGrantedAuthority(e)));
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public User setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public User setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public User setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }
}
