package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

public class MSProduct implements MSProductIdentity {

    private String id;
    private String name;
    private MSGenericNameRow msGenericNameRow;
    private MSProductRow msProductRow;

    public MSProduct(MSProductRow msProductRow, MSGenericNameRow msGenericNameRow) {
        this.id = msProductRow.getId();
        this.name = msGenericNameRow.getName();
        this.msProductRow = msProductRow;
        this.msGenericNameRow = msGenericNameRow;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public MSGenericNameRow getMsGenericNameRow() {
        return msGenericNameRow;
    }

    public MSProductRow getMsProductRow() {
        return msProductRow;
    }

}
