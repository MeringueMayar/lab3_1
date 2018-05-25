/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;


public class ProductDataBuilder {
    private Id productId;
    private Money price;
    private String name;
    private ProductType type;
    private Date snapshotDate;

    public ProductDataBuilder() {
    }

    public ProductDataBuilder setProductId(Id productId) {
        this.productId = productId;
        return this;
    }

    public ProductDataBuilder setPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder setType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductDataBuilder setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public ProductData createProductData() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }
    
}
