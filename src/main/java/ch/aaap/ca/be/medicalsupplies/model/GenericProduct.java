package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;

import java.util.Objects;

public class GenericProduct {

    private String genericName;

    public GenericProduct(MSGenericNameRow genericNameRow) {
        this.genericName = genericNameRow.getName();
    }

    public String getGenericName() {
        return genericName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericProduct)) return false;
        GenericProduct that = (GenericProduct) o;
        return genericName.equalsIgnoreCase(that.genericName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genericName);
    }
}
