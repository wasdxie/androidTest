package com.qianmi.weidian.domain;

import java.util.ArrayList;

/**
 * Created by xiedejun on 2018/2/26.
 */

public class ProductList {
    private ArrayList<ProductBo> dataList;

    public ArrayList<ProductBo> getProductBos() {
        return dataList;
    }

    public void setProductBos(ArrayList<ProductBo> productBos) {
        this.dataList = productBos;
    }
}
