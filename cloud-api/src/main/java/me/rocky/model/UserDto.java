package me.rocky.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/18 16:05
 * @log
 */
public class UserDto implements Serializable {
	private static final long serialVersionUID = -7360011061077328325L;
	private Long id;
	private String username;
	private String password;
	private Integer status;
	private String clientId;
	private List<String> roles;

	public Long getId() {
		return id;
	}

	public UserDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public UserDto setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserDto setPassword(String password) {
		this.password = password;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public UserDto setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public UserDto setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public List<String> getRoles() {
		return roles;
	}

	public UserDto setRoles(List<String> roles) {
		this.roles = roles;
		return this;
	}
}
