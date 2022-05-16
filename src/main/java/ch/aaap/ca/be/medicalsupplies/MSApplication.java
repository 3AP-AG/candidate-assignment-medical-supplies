package ch.aaap.ca.be.medicalsupplies;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.model.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.model.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.MSStats;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MSApplication {

    public static final String HAS_GENERIC_NAME = "hasGenericName";
    public static final String NO_GENERIC_NAME = "noGenericName";
    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;
//    private final MSProductDTO msProductDTOS;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();
//        msProductDTOS = createModel(genericNames, registry);
    }

    public static void main(String[] args) {
        MSApplication main = new MSApplication();

        System.err.println("generic names count: " + main.genericNames.size());
        System.err.println("registry count: " + main.registry.size());

        System.err.println("1st of generic name list: " + main.genericNames.iterator().next());
        System.err.println("1st of registry list: " + main.registry.iterator().next());

        MSStats msProductDTOS = main.createModel(main.genericNames, main.registry);

        main.numberOfUniqueGenericNames(msProductDTOS);
        main.numberOfDuplicateGenericNames(msProductDTOS);

        main.numberOfMSProductsWithGenericName(msProductDTOS);
        main.numberOfMSProductsWithoutGenericName(msProductDTOS);

        main.nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts(msProductDTOS);
        main.numberOfMSProductsByProducerName(msProductDTOS, "roche");

        main.findMSProductsWithGenericNameCategory(main.genericNames, main.registry, "05 - Bolnička, aparaturna oprema");
    }

    /**
     * Create a model / data structure that combines the input sets.
     *
     * @param genericNameRows
     * @param productRows
     * @return
     */
    public MSStats createModel(final Set<MSGenericNameRow> genericNameRows, final Set<MSProductRow> productRows) {

        if (genericNameRows == null || genericNameRows.size() == 0 || productRows == null || productRows.size() == 0) {
            throw new MSException(MSException.NO_DATA);
        }

        //I am aware this can be done in one pass
        return MSStats.builder()
                .genericNameOccurrenceStats(generateNameOccurrenceStats(genericNameRows))
                .genericNameExistsStats(generateNameExistsStats(genericNameRows, productRows))
                .producerProductCountStats(generateProducerProductStats(productRows))
                .producerLicenseProductCountStats(generateSameProducerLicenceNameStats(productRows))
                .build();
    }
    /* MS Generic Names */

    /**
     * Method find the number of unique generic names.
     *
     * @return
     */
    public Object numberOfUniqueGenericNames(final MSStats msProductDTOS) {

        return msProductDTOS.getGenericNameOccurrenceStats().size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     *
     * @return
     */
    public Object numberOfDuplicateGenericNames(final MSStats msProductDTOS) {

        return msProductDTOS.getGenericNameOccurrenceStats().entrySet().stream()
                .filter(entry -> entry.getValue().get() > 1).count();
    }

    /* MS Products */

    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithGenericName(final MSStats msProductDTOS) {
        return msProductDTOS.getGenericNameExistsStats().get(HAS_GENERIC_NAME).get();
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName(final MSStats msProductDTOS) {
        return msProductDTOS.getGenericNameExistsStats().get(NO_GENERIC_NAME).get();
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     *
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts(final MSStats msProductDTOS) {
        return msProductDTOS.getProducerLicenseProductCountStats().entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingInt(AtomicInteger::get)))
                .map(Map.Entry::getKey)
                .orElse("");
    }

    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     *
     * @param companyName
     * @return
     */
    public Object numberOfMSProductsByProducerName(final MSStats msProductDTOS, final String companyName) {
        return msProductDTOS.getProducerProductCountStats().entrySet().stream()
                .filter(entry -> entry.getKey().toUpperCase().contains(companyName.toUpperCase()))
                .mapToInt(entry -> entry.getValue().get()).sum();
    }

    /**
     * Method finds the products whose generic name has the category of interest.
     *
     * @param category
     * @return
     */
    //TODO: Move to model
    public int findMSProductsWithGenericNameCategory(final Set<MSGenericNameRow> genericNameRows, final Set<MSProductRow> productRows, final String category) {

        if (category == null || category.isEmpty()) {
            return 0;
        }

        AtomicInteger i = new AtomicInteger();

        for (MSProductRow msProductRow : productRows) {

            genericNameRows.stream()
                    .filter(msGenericNameRow -> msGenericNameRow.getName().equals(msProductRow.getGenericName()))
                    .filter(msGenericNameRow -> msGenericNameRow.hasCategory(category))
                    .findFirst()
                    .map(msGenericNameRow -> i.getAndIncrement());
        }
        return i.get();
    }


    private Map<String, AtomicInteger> generateProducerProductStats(Set<MSProductRow> productRows) {
        Map<String, AtomicInteger> producerStats = new HashMap<>();

        for (MSProductRow msProductRow : productRows) {

            producerStats.putIfAbsent(msProductRow.getProducerName(), new AtomicInteger(0));
            producerStats.get(msProductRow.getProducerName()).getAndIncrement();
        }
        return producerStats;
    }

    private Map<String, AtomicInteger> generateSameProducerLicenceNameStats(Set<MSProductRow> productRows) {
        Map<String, AtomicInteger> sameProducerLicenceNameStats = new HashMap<>();

        for (MSProductRow msProductRow : productRows) {

            if (msProductRow.getProducerName().equals(msProductRow.getLicenseHolderName())) {
                sameProducerLicenceNameStats.putIfAbsent(msProductRow.getProducerName(), new AtomicInteger(0));
                sameProducerLicenceNameStats.get(msProductRow.getProducerName()).getAndIncrement();
            }
        }
        return sameProducerLicenceNameStats;
    }


    private Map<String, AtomicInteger> generateNameExistsStats(final Set<MSGenericNameRow> genericNameRows, final Set<MSProductRow> productRows) {

        Map<String, AtomicInteger> productStats = new ConcurrentHashMap<>();
        productStats.put(HAS_GENERIC_NAME, new AtomicInteger(0));
        productStats.put(NO_GENERIC_NAME, new AtomicInteger(0));

        for (MSProductRow msProductRow : productRows) {

            genericNameRows.stream()
                    .filter(msGenericNameRow -> msGenericNameRow.getName().equals(msProductRow.getGenericName()))
                    .findFirst()
                    .map(msGenericNameRow -> productStats.get(HAS_GENERIC_NAME).getAndIncrement())
                    .orElseGet(() -> productStats.get(NO_GENERIC_NAME).getAndIncrement());
        }
        return productStats;

    }

    private Map<String, AtomicInteger> generateNameOccurrenceStats(Set<MSGenericNameRow> genericNameRows) {
        Map<String, AtomicInteger> nameStats = new HashMap<>();

        for (MSGenericNameRow msGenericNameRow : genericNameRows) {
            nameStats.putIfAbsent(msGenericNameRow.getName(), new AtomicInteger(0));
            nameStats.get(msGenericNameRow.getName()).getAndIncrement();
        }
        return nameStats;
    }

}
