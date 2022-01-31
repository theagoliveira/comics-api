package br.com.zup.comicsapi.marvel;

import java.math.BigDecimal;

public class MarvelPrice {

    private String type;
    private BigDecimal price;

    public MarvelPrice() {}

    public MarvelPrice(String type, BigDecimal price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
