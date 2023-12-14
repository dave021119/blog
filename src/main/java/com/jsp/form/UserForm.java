package com.jsp.form;

import java.util.Date;

public class UserForm {
    private Integer id = 0;
    private String account = null;
    private String password = null;
    private String name = null;
    private String role;
    private Date createTime;
    private String email;


    public UserForm() {
    }

    public Integer getId () {
        return this.id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getAccount () {
        return this.account;
    }

    public void setAccount (String account) {
        this.account = account;
    }

    public String getPassword () {
        return this.password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
