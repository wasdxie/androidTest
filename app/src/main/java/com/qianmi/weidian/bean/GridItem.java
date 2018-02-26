package com.qianmi.weidian.bean;

import com.qianmi.weidian.domain.ProductBo;

public class GridItem {
    private String imageUri;
    private String title;

    private ProductBo productBo;


    public GridItem() {
        super();
    }

    public String getImage() {
        return imageUri;
    }

    public void setImage(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductBo getProductBo() {
        return productBo;
    }

    public void setProductBo(ProductBo productBo) {
        this.productBo = productBo;
    }
}