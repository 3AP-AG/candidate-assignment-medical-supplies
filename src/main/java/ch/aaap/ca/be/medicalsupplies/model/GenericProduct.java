package ch.aaap.ca.be.medicalsupplies.model;

public class GenericProduct {

    private String genericName;

    public GenericProduct(String genericName) {
        this.genericName = genericName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}
