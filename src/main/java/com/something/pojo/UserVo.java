package com.something.pojo;

import org.apache.ibatis.mapping.FetchType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * UserVo
 */
public class UserVo implements Serializable {

    /**
     * username
     */
    @NotBlank(message = "username not blank")
    private String username;
    /**
     * password
     */
    @NotBlank(message = "password not blank")
    private String password;
    /**
     * new password
     */
    private String newPassword;
    /**
     * name
     */
    private String name;
    /**
     * email
     */
    private String email;
    /**
     * captchaCode
     */
    private String captchaCode;

    /**
     * roles
     */
    List<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
