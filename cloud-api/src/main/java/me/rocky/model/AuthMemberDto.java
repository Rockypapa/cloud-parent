package me.rocky.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/18 16:10
 * @log
 */
public class AuthMemberDto implements Serializable {
	private static final long serialVersionUID = 7695099412251262892L;

	private Long id;
	private String username;
	private String password;
	private Boolean status;
	private String clientId;
	private String avatar;
	private String nickname;
	private List<String> roles;

	public Long getId() {
		return id;
	}

	public AuthMemberDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public AuthMemberDto setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public AuthMemberDto setPassword(String password) {
		this.password = password;
		return this;
	}

	public Boolean getStatus() {
		return status;
	}

	public AuthMemberDto setStatus(Boolean status) {
		this.status = status;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public AuthMemberDto setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public String getAvatar() {
		return avatar;
	}

	public AuthMemberDto setAvatar(String avatar) {
		this.avatar = avatar;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public AuthMemberDto setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public List<String> getRoles() {
		return roles;
	}

	public AuthMemberDto setRoles(List<String> roles) {
		this.roles = roles;
		return this;
	}
}
