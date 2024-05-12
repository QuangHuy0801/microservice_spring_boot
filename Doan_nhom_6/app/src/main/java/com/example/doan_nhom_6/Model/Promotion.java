package com.example.doan_nhom_6.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Promotion implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("startDate")
    @Expose
    private Date startDate;

    @SerializedName("endDate")
    @Expose
    private Date endDate;

    @SerializedName("discountPercent")
    @Expose
    private double discountPercent;

    @SerializedName("status")
    @Expose
    private String status;

//    @SerializedName("promotionProducts")
//    @Expose
    private List<Promotion_Item> promotion_Item;

    public Promotion(int id, String name, String description, Date startDate, Date endDate, double discountPercent, String status, List<Promotion_Item> promotion_Item) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercent = discountPercent;
        this.status = status;
        this.promotion_Item = promotion_Item;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", discountPercent=" + discountPercent +
                ", status='" + status + '\'' +
                ", promotion_Item=" + promotion_Item +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Promotion_Item> getPromotion_Item() {
        return promotion_Item;
    }

    public void setPromotion_Item(List<Promotion_Item> promotion_Item) {
        this.promotion_Item = promotion_Item;
    }
}
