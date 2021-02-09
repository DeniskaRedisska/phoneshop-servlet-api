package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceInfo {
    private LocalDate date;
    private BigDecimal price;

    public PriceInfo(LocalDate date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
