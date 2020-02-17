package edu.uci.thanote.databases.general;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * BaseTable is a table that contains basic fields
 * <p>1. id: Primary Key, auto increased</p>
 * <p>2. createDate: Date when this object is created</p>
 */
public class BaseTable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "created_date")
    private Date createDate;

    public BaseTable() {
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
}
