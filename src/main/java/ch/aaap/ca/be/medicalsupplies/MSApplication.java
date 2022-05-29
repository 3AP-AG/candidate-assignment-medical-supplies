package ch.aaap.ca.be.medicalsupplies;

import java.util.*;
import java.util.stream.Collectors;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.MSProduct;
import ch.aaap.ca.be.medicalsupplies.model.Producer;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;

    private final HashMap<MSProductIdentity, MSProduct> msProductMap;
    private Map<String, String[]> msGenericNameHashMap;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();
        msProductMap = (HashMap<MSProductIdentity, MSProduct>)createModel(registry, genericNames);
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

    public Object createModel(Set<MSProductRow> productRows, Set<MSGenericNameRow> genericNameRows) {

        Map<MSProductIdentity, MSProduct> productMap = new HashMap<>();
        msGenericNameHashMap  = createGenericNameMap(genericNameRows);

        try {
            for (MSProductRow msProductRow: productRows) {
                MSProduct msProduct = new MSProduct(msProductRow);
                String[] categories = msGenericNameHashMap.get(msProduct.getProductIdentity().getName());
                if (categories != null) {
                    if (!categories[0].equals(msProductRow.getPrimaryCategory()))
                        System.out.println("WARING: Product: [" + msProductRow.getGenericName() + "] doesn't have same primary category as category1 from Generic data file, categories will be taken from Generic datafile." );
                    msProduct.setCategories(categories);
                }
                productMap.put(msProduct.getProductIdentity(), msProduct);
            }
        } catch (Exception e) {
            throw new MSException(MSException.PRODUCT_CONSOLIDATION_MESSAGE);
        }
        return productMap;
    }

    private Map<String, String[]> createGenericNameMap(Set<MSGenericNameRow> genericNameRows) {
        Map<String, String[]> msGenericNameRowHashMap = new HashMap<>();

        genericNameRows.forEach(genericName -> msGenericNameRowHashMap.put(genericName.getName(), msCategories(genericName)));

        return msGenericNameRowHashMap;
    }

    private String[] msCategories(MSGenericNameRow msGenericNameRow) {
        String[]categories = new String[4];

        categories[0] = msGenericNameRow.getCategory1();
        categories[1] = msGenericNameRow.getCategory2();
        categories[2] = msGenericNameRow.getCategory3();
        categories[3] = msGenericNameRow.getCategory4();
        return categories;
    }
    /* MS Generic Names */
    /**
     * Method find the number of unique generic names.
     *
     * @return
     */
    public Object numberOfUniqueGenericNames() {
        return msGenericNameHashMap.size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     *
     * @return
     */
    public Object numberOfDuplicateGenericNames() {
        return genericNames.size() - msGenericNameHashMap.size();
    }

    /* MS Products */
    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithGenericName() {
        return msProductMap.entrySet().stream().filter(product -> product.getValue().getCategories() != null).collect(Collectors.toList()).size();
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     *
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName() {
        return msProductMap.entrySet().stream().filter(product -> product.getValue().getCategories() == null).collect(Collectors.toList()).size();
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     *
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {
        Map<Producer,List<MSProduct>> filteredProducts = msProductMap.entrySet().stream().map(mapEl -> mapEl.getValue()).filter(product -> product.getProducer().getProducerId().equalsIgnoreCase(product.getLicenseHolder().getLicenseHolderId()))
                .collect(Collectors.groupingBy(MSProduct::getProducer));
        String companyName = null;
        int maxMSProducts = 0;

        for (Map.Entry<Producer,List<MSProduct>> producerEntry : filteredProducts.entrySet()) {
            if (producerEntry.getValue().size() > maxMSProducts) {
                maxMSProducts = producerEntry.getValue().size();
                companyName = producerEntry.getKey().getProducerName();
            }
        }
        return companyName;
    }

    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     *
     * @param companyName
     * @return
     */
    public Object numberOfMSProductsByProducerName(String companyName) {
        return msProductMap.entrySet().stream().filter(product -> product.getValue().getProducer().getProducerName().toLowerCase().startsWith(companyName)).collect(Collectors.toList()).size();
    }

    /**
     * Method finds the products whose generic name has the category of interest.
     *
     * @param category
     * @return
     */
    public Set<MSProductIdentity> findMSProductsWithGenericNameCategory(String category) {
        return msProductMap.entrySet().stream().filter(product -> hasCategory(product.getValue().getCategories(), category)).map(p -> p.getKey()).collect(Collectors.toSet());
    }

    boolean hasCategory (String[] categories, String categoryName) {
        if (categories == null)
            return false;
        else {
              for (int i = 0; i < 4; i++)
                  if (categories[i].equalsIgnoreCase(categoryName))
                      return true;
        }
        return false;
    }
}
