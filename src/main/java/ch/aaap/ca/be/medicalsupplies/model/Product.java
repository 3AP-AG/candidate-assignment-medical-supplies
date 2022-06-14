package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

import java.util.Map;

public class Product implements MSProductIdentity {

    private String id;
    private GenericProduct genericProduct;
    private String name;
    private Category primaryCategory;
    private Company producer;
    private Company licenseHolder;

    public Product(MSProductRow productRow, Map<String, GenericProduct> genericProducts, Map<String, Category> categories) {
        this.id = productRow.getId();
        this.genericProduct = genericProducts.get(productRow.getGenericName());
        this.name = productRow.getName();
        this.primaryCategory = categories.get(productRow.getPrimaryCategory());
        this.producer = new Company(Integer.valueOf(productRow.getProducerId()), productRow.getProducerName(), productRow.getProducerAddress());
        this.licenseHolder = new Company(Integer.valueOf(productRow.getLicenseHolderId()), productRow.getLicenseHolderName(), productRow.getLicenseHolderAddress());
    }

    public String getId() {
        return id;
    }

    public GenericProduct getGenericProduct() {
        return genericProduct;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public Company getProducer() {
        return producer;
    }

    public Company getLicenseHolder() {
        return licenseHolder;
    }
}
