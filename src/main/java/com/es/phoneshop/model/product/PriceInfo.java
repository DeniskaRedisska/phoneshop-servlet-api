package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PriceInfo {
    private Date date;
    private BigDecimal price;

    public PriceInfo(Date date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
