package edu.uci.thanote.sqlite.models;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {
    private int id;
    private Date createDate;

    public BaseModel() {
        this.createDate = new Date();
    }

    public int getId() {
        return id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id=" + id +
                ", createDate=" + createDate +
                '}';
    }
}
