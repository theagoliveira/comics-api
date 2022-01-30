package br.com.zup.comicsapi.marvel;

public class MarvelPrice {

    private String type;
    private Double price;

    public MarvelPrice() {}

    public MarvelPrice(String type, Double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

}
