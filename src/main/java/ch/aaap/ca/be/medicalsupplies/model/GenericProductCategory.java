package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

public class GenericProductCategory {

    private Integer id;
    private GenericProduct genericProduct;
    private Category category;

    public GenericProductCategory(Integer id, GenericProduct genericProduct, Category category) {
        this.id = id;
        this.genericProduct = genericProduct;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GenericProduct getGenericProduct() {
        return genericProduct;
    }

    public void setGenericProduct(GenericProduct genericProduct) {
        this.genericProduct = genericProduct;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericProductCategory)) return false;
        GenericProductCategory that = (GenericProductCategory) o;
        return Objects.equals(genericProduct, that.genericProduct) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genericProduct, category);
    }
}
