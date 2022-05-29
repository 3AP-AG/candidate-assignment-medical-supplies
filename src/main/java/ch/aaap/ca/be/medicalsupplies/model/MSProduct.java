package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

public class MSProduct {
    private ProductIdentity productIdentity;
    private Producer producer;
    private LicenseHolder licenseHolder;
    private Model model;
    private String[] categories;

    public MSProduct(MSProductRow msProductRow) {
        this.productIdentity = new ProductIdentity(msProductRow);
        this.producer = new Producer(msProductRow);
        this.licenseHolder = new LicenseHolder(msProductRow);
        this.model = new Model(msProductRow);
    }

    public ProductIdentity getProductIdentity() {
        return productIdentity;
    }

    public Producer getProducer() {
        return producer;
    }

    public LicenseHolder getLicenseHolder() {
        return licenseHolder;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
}
