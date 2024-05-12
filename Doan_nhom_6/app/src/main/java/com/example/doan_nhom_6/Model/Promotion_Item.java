package com.example.doan_nhom_6.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Promotion_Item implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    //	@SerializedName("product")
//	@Expose
    private Product product;

    //	@SerializedName("order")
//	@Expose
    private Promotion promotion;

    public Promotion_Item(int id, Product product, Promotion promotion) {
        this.id = id;
        this.product = product;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "Promotion_Item{" +
                "id=" + id +
                ", product=" + product +
                ", promotion=" + promotion +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
