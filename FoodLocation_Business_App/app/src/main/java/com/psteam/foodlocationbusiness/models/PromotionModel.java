package com.psteam.foodlocationbusiness.models;

public class PromotionModel {
    private int image;
    private String foodName;
    private String contentPromotion;


    public PromotionModel(int image, String foodName, String contentPromotion) {
        this.image = image;
        this.foodName = foodName;
        this.contentPromotion = contentPromotion;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getContentPromotion() {
        return contentPromotion;
    }

    public void setContentPromotion(String contentPromotion) {
        this.contentPromotion = contentPromotion;
    }
}
