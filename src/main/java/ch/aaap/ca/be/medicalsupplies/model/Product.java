package ch.aaap.ca.be.medicalsupplies.model;

public class Product {

    private String id;
    private GenericProduct genericProduct;
    private String name;
    private Category primaryCategory;
    private Company producer;
    private Company licenseHolder;

    public Product(String id, GenericProduct genericProduct, String name, Category primaryCategory, Company producer, Company licenseHolder) {
        this.id = id;
        this.genericProduct = genericProduct;
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.producer = producer;
        this.licenseHolder = licenseHolder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GenericProduct getGenericProduct() {
        return genericProduct;
    }

    public void setGenericProduct(GenericProduct genericProduct) {
        this.genericProduct = genericProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public Company getProducer() {
        return producer;
    }

    public void setProducer(Company producer) {
        this.producer = producer;
    }

    public Company getLicenseHolder() {
        return licenseHolder;
    }

    public void setLicenseHolder(Company licenseHolder) {
        this.licenseHolder = licenseHolder;
    }
}
