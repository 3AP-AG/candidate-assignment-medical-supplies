package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;

import java.util.*;

public class GenericProductCategory {
    private GenericProduct genericProduct;
    private Category category;
    private Set<Category> categories;

    public GenericProductCategory(MSGenericNameRow genericNameRow, Set<Category> categoriesSet) {
        this.genericProduct = new GenericProduct(genericNameRow);
        this.categories = categoriesSet;
    }

    public GenericProduct getGenericProduct() {
        return genericProduct;
    }

    public Category getCategory() {
        return category;
    }

    public Set<Category> getCategories() {
        return categories;
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
