package com.jsp.form;

public class ArticleTypeForm {
    private Integer id = - 1;
    private String typeName = "";
    private String description = "";
    private int number = 0;

    public int getNumber () {
        return number;
    }

    public void setNumber (int number) {
        this.number = number;
    }


    public ArticleTypeForm () {
    }

    public String getDescription () {
        return this.description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public Integer getId () {
        return this.id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getTypeName () {
        return this.typeName;
    }

    public void setTypeName (String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString () {
        return "ArticleTypeForm{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", description='" + description + '\'' +
                ", number=" + number +
                '}';
    }
}
