package ch.aaap.ca.be.medicalsupplies.model;

public class Category {

    private String code;
    private String name;

    public Category(String category) {
        this.code = category != null ? category.substring(0, 2) : null;
        this.name = category != null ? category.substring(5, category.length()) : null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
