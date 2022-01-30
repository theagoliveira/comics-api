package br.com.zup.comicsapi.marvel;

public class MarvelCreatorsItem {

    private String name;
    private String role;

    public MarvelCreatorsItem() {}

    public MarvelCreatorsItem(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

}
