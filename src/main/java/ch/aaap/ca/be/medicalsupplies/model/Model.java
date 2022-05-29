package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

import java.util.Objects;

public class Model {
    private String id;
    private String[] modelList;
    private String[] typeList;

    public Model(MSProductRow msProductRow) {
        int modelIndex = msProductRow.getName().indexOf("Model:");
        int typeIndex = msProductRow.getName().indexOf("Tip:");
        if (modelIndex > 0) {
            id = msProductRow.getId();
            modelList = msProductRow.getName().substring(modelIndex + 6, msProductRow.getName().length()).split(";");
        } else if (typeIndex > 0) {
            id = msProductRow.getId();
            typeList = msProductRow.getName().substring(typeIndex + 4, msProductRow.getName().length()).split(";");
        }
    }
}
