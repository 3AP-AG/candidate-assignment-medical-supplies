package ch.aaap.ca.be.medicalsupplies;

import java.util.*;
import java.util.stream.Collectors;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.MSProduct;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;

    private final Map<String, MSGenericNameRow> msGenericNameRowHashMap;
    private final List<MSProduct> msProductList;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();
        msGenericNameRowHashMap  = createGenericNameRowMap();
        msProductList = (List<MSProduct>) createModel(genericNames, registry);
    }

    public static void main(String[] args) {
        MSApplication main = new MSApplication();

        System.err.println("generic names count: " + main.genericNames.size());
        System.err.println("registry count: " + main.registry.size());

        System.err.println("1st of generic name list: " + main.genericNames.iterator().next());
        System.err.println("1st of registry list: " + main.registry.iterator().next());
    }

    /**
     * Create a model / data structure that combines the input sets.
     *
     * @param genericNameRows
     * @param productRows
     * @return
     */
    public Object createModel(Set<MSGenericNameRow> genericNameRows, Set<MSProductRow> productRows) {

        List<MSProduct> productList = new ArrayList<>();

        try {
            productRows.forEach(productRow -> {
                if (productRow.getGenericName() != null && msGenericNameRowHashMap.get(productRow.getGenericName()) != null) {
                    productList.add(new MSProduct(productRow, msGenericNameRowHashMap.get(productRow.getGenericName())));
                }
            });
        } catch (Exception e) {
            throw new MSException(MSException.PRODUCT_CONSOLIDATION_MESSAGE);
        }
        return productList;
    }

    private Map<String, MSGenericNameRow> createGenericNameRowMap() {

        // map to quickly access  MSGenericNameRow element by Name
        Map<String, MSGenericNameRow> msGenericNameRowHashMap = new HashMap<>();

        // temporary needed to filter out duplicate Names from genericNames
        Set<String> distinctGNames = new HashSet<>();

        genericNames.forEach(genericName -> {
            if (genericName.getName() != null && !distinctGNames.contains(genericName.getName())) {
                distinctGNames.add(genericName.getName());
                msGenericNameRowHashMap.put(genericName.getName(), genericName);
            }
        });

        return msGenericNameRowHashMap;
    }

    /* MS Generic Names */
    /**
     * Method find the number of unique generic names.
     *
     * @return
     */
    public Object numberOfUniqueGenericNames() {
        return msGenericNameRowHashMap.size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     *
     * @return
     */
    public Object numberOfDuplicateGenericNames() {
        return genericNames.size() - msGenericNameRowHashMap.size();
    }

    /* MS Products */
    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithGenericName() {
        return msProductList.size();
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName() {
        return registry.size() - msProductList.size();
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     *
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {
        List<MSProductRow> producersAndLicenseHolders = registry.stream().filter(product -> product.getProducerId().equalsIgnoreCase(product.getLicenseHolderId())).collect(Collectors.toList());
        Map<String, List<MSProductRow>> groupedProductsByProducerId = producersAndLicenseHolders.stream().collect(Collectors.groupingBy(MSProductRow::getProducerId));
        Map<String, Integer> countedProductsByProducerId = groupedProductsByProducerId.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().size()));
        int max = 0;
        String producerId = null;

        for (Map.Entry<String, Integer> entry : countedProductsByProducerId.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                producerId = entry.getKey();
            }
        }
        return groupedProductsByProducerId.get(producerId).get(0).getProducerName();
    }
    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     *
     * @param companyName
     * @return
     */
    public Object numberOfMSProductsByProducerName(String companyName) {
        return registry.stream().filter(product -> product.getProducerName().toLowerCase().startsWith(companyName)).collect(Collectors.toList()).size();
    }

    /**
     * Method finds the products whose generic name has the category of interest.
     *
     * @param category
     * @return
     */
    public Set<MSProductIdentity> findMSProductsWithGenericNameCategory(String category) {
        return msProductList.stream().filter(msProduct ->  msProduct.getMsGenericNameRow().getCategory1().equalsIgnoreCase(category)
                || msProduct.getMsGenericNameRow().getCategory2().equalsIgnoreCase(category)
                || msProduct.getMsGenericNameRow().getCategory3().equalsIgnoreCase(category)
                || msProduct.getMsGenericNameRow().getCategory4().equalsIgnoreCase(category)).collect(Collectors.toSet());
    }
}
