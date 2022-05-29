package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

public class ProductIdentity implements MSProductIdentity {
    private String id;
    private String name;
    private String genericName;

    public ProductIdentity(MSProductRow msProductRow) {
        this.id = msProductRow.getId();
        this.name = msProductRow.getName();
        this.genericName = msProductRow.getGenericName();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return genericName;
    }

}
