package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

public class Category {

    private String code;
    private String name;

    public Category(String category) {
        this.code = (category != null && !category.isEmpty()) ? category.substring(0, 2) : null;
        this.name = (category != null && !category.isEmpty()) ? category.substring(5, category.length()) : null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return code.equalsIgnoreCase(category.code) && name.equalsIgnoreCase(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
