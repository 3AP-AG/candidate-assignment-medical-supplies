package ch.aaap.ca.be.medicalsupplies;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.model.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.model.MSProduct;
import ch.aaap.ca.be.medicalsupplies.model.MSProductRow;

import java.util.*;
import java.util.stream.Collectors;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;
    private final Set<MSProduct> msProducts;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();
        msProducts = createModel(genericNames, registry);
    }

    public static void main(String[] args) {
        MSApplication main = new MSApplication();

        System.err.println("generic names count: " + main.genericNames.size());
        System.err.println("registry count: " + main.registry.size());

        System.err.println("1st of generic name list: " + main.genericNames.iterator().next());
        System.err.println("1st of registry list: " + main.registry.iterator().next());

        main.numberOfUniqueGenericNames();
        main.numberOfDuplicateGenericNames();

        main.numberOfMSProductsWithGenericName();
        main.numberOfMSProductsWithoutGenericName();

        main.nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts();
        main.numberOfMSProductsByProducerName("roche");

        main.findMSProductsWithGenericNameCategory("05 - Bolnička, aparaturna oprema");
    }

    /**
     * Create a model / data structure that combines the input sets.
     *
     * @param genericNameRows
     * @param productRows
     * @return
     */
    public Set<MSProduct> createModel(final Set<MSGenericNameRow> genericNameRows,final Set<MSProductRow> productRows) {

        if (genericNameRows == null || genericNameRows.size() == 0 || productRows == null || productRows.size() == 0) {
            throw new MSException(MSException.NO_DATA);
        }

        Set<MSProduct> msProducts = new HashSet<>();

        for (MSProductRow productRow : productRows) {

            MSGenericNameRow nameRow = genericNameRows.stream()
                    .filter(msGenericNameRow -> msGenericNameRow.getName().equals(productRow.getGenericName()))
                    .findFirst()
                    .orElse(null);

            MSProduct msProduct = MSProduct.builder()
                    .id(productRow.getId())
                    .producerId(productRow.getProducerId())
                    .producerName(productRow.getProducerName())
                    .licenceHolderId(productRow.getLicenseHolderId())
                    .licenceHolderName(productRow.getLicenseHolderName())
                    .genericName(nameRow != null ? nameRow.getName() : null)
                    .categories(nameRow != null ? nameRow.getCategories() : null)
                    .build();

            msProducts.add(msProduct);
        }

        return msProducts;
    }

    /* MS Generic Names */

    /**
     * Method finds the number of unique generic names.
     *
     * @return
     */
    public Object numberOfUniqueGenericNames() {

        Map<String, Integer> uniquesProducts = new HashMap<>();
        genericNames.forEach(msProduct -> uniquesProducts.merge(msProduct.getName(), 1, Integer::sum));

        return uniquesProducts.size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     *
     * @return
     */
    public Object numberOfDuplicateGenericNames() {

        return genericNames.size() - (int) numberOfUniqueGenericNames();
    }

    /* MS Products */

    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithGenericName() {
        return (int) msProducts.stream()
                .filter(msProduct -> msProduct.getGenericName() != null)
                .count();
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName() {
        return (int) msProducts.stream()
                .filter(msProduct -> msProduct.getGenericName() == null)
                .count();
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * maximum number of products.
     *
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {

        return msProducts.stream()
                .filter(msProduct -> msProduct.getProducerName().equals(msProduct.getLicenceHolderName()))
                .collect(Collectors.groupingBy(MSProduct::getProducerName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingInt(Long::intValue)))
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
    public Object numberOfMSProductsByProducerName(final String companyName) {

        if (companyName == null || companyName.isEmpty()) {
            return "";
        }

        return msProducts.stream()
                .filter(msProduct -> (msProduct.getProducerName()).toUpperCase().startsWith(companyName.toUpperCase()))
                .collect(Collectors.toSet()).size();
    }

    /**
     * Method finds the products whose generic name has the category of interest.
     *
     * @param category
     * @return
     */
    public Set<MSProductIdentity> findMSProductsWithGenericNameCategory(final String category) {

        if (category == null || category.isEmpty()) {
            return Collections.EMPTY_SET;
        }

        return msProducts.stream()
                .filter(msProduct -> msProduct.getCategories() != null)
                .filter(msProduct -> msProduct.getCategories().contains(category))
                .collect(Collectors.toSet());
    }
}
