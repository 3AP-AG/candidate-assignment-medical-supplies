package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MSProduct implements MSProductIdentity {

    private String id;
    //One or two names?
    private String genericName;

    private String producerId;
    private String producerName;
    private String licenceHolderId;
    private String licenceHolderName;
    private ArrayList<String> categories = new ArrayList<>();


    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return getGenericName();
    }
}
