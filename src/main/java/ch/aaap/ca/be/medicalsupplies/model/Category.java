package ch.aaap.ca.be.medicalsupplies.model;

public class Category {

    private String code;
    private String name;

    public Category(String category) {
        this.code = category.substring(0, 2);
        this.name = category.substring(5, category.length());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
