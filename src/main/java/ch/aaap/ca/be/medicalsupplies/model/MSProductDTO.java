package ch.aaap.ca.be.medicalsupplies.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MSProductDTO {

    Map<String, AtomicInteger> nameStats = new HashMap<>();
    Map<String, AtomicInteger> productGenericNameStats = new HashMap<>();
    Map<String, AtomicInteger> producerLicenseHolderStats = new HashMap<>();
    Map<String, AtomicInteger> producerStats = new HashMap<>();

    private MSProductRow msProductRow;
    private MSGenericNameRow msGenericNameRow;


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MSProductDTO)) {
            return false;
        }

        MSProductDTO msProductDTO = (MSProductDTO) obj;

        return msProductDTO.getMsProductRow().getGenericName().equals(this.getMsProductRow().getGenericName());
    }

}
