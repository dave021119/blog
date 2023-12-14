package com.jsp.form;



public class DataForm {
    private Integer id;

    private Integer userId;
    private Integer aId;
    private Integer type;

    public DataForm(){

    }
    public DataForm(Integer userId, Integer aId, int i) {
        this.userId=userId;
        this.aId=aId;
        this.type=i;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
