package ch.aaap.ca.be.medicalsupplies.model;

import java.util.*;

public class GenericProductCategory {
    private GenericProduct genericProduct;
    private Set<Category> categories;

    public GenericProductCategory(GenericProduct genericProduct, Set<Category> categoriesSet) {
        this.genericProduct = genericProduct;
        this.categories = categoriesSet;
    }

    public GenericProduct getGenericProduct() {
        return genericProduct;
    }

    public Set<Category> getCategories() {
        return categories;
    }
}
