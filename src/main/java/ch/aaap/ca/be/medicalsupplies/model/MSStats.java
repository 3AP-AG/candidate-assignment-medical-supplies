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
public class MSStats {

    Map<String, AtomicInteger> genericNameOccurrenceStats = new HashMap<>();
    //EnumMap?
    Map<String, AtomicInteger> genericNameExistsStats = new HashMap<>();
    Map<String, AtomicInteger> producerLicenseProductCountStats = new HashMap<>();
    Map<String, AtomicInteger> producerProductCountStats = new HashMap<>();

    private MSProductRow msProductRow;
    private MSGenericNameRow msGenericNameRow;


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MSStats)) {
            return false;
        }

        MSStats msStats = (MSStats) obj;

        return msStats.getMsProductRow().getGenericName().equals(this.getMsProductRow().getGenericName());
    }

}
