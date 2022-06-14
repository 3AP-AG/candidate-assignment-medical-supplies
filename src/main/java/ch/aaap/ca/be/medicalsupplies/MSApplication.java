package ch.aaap.ca.be.medicalsupplies;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.*;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;

    private static Map<String, GenericProduct> genericProducts;
    private static Map<String, Product> products;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();

        createModel(genericNames, registry);
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

        Map<String, Category> categories = new HashMap<>();
        genericProducts = new HashMap<>();

        final AtomicInteger genericProductCategoryId = new AtomicInteger(0);
        Set<GenericProductCategory> genericProductCategories = new HashSet<>();

        genericNameRows.forEach(genericNameRow -> {

            // get categories
            String category1String = (genericNameRow.getCategory1() == null || genericNameRow.getCategory1().isEmpty()) ? null : genericNameRow.getCategory1();
            String category2String = (genericNameRow.getCategory2() == null || genericNameRow.getCategory2().isEmpty()) ? null : genericNameRow.getCategory2();
            String category3String = (genericNameRow.getCategory3() == null || genericNameRow.getCategory3().isEmpty()) ? null : genericNameRow.getCategory3();
            String category4String = (genericNameRow.getCategory4() == null || genericNameRow.getCategory4().isEmpty()) ? null : genericNameRow.getCategory4();

            Category category1 = category1String == null ? null : new Category(category1String);
            Category category2 = category2String == null ? null : new Category(category2String);
            Category category3 = category3String == null ? null : new Category(category3String);
            Category category4 = category4String == null ? null : new Category(category4String);

            if (category1String != null) categories.put(category1String, category1);
            if (category2String != null) categories.put(category2String, category2);
            if (category3String != null) categories.put(category3String, category3);
            if (category4String != null) categories.put(category4String, category4);

            // get generic products
            String genericName = genericNameRow.getName();
            GenericProduct genericProduct = new GenericProduct(genericName);
            genericProducts.put(genericName, genericProduct);

            // get generic product category
            if (category1String != null) genericProductCategories.add(new GenericProductCategory(genericProductCategoryId.incrementAndGet(), genericProduct, category1));
            if (category2String != null) genericProductCategories.add(new GenericProductCategory(genericProductCategoryId.incrementAndGet(), genericProduct, category2));
            if (category3String != null) genericProductCategories.add(new GenericProductCategory(genericProductCategoryId.incrementAndGet(), genericProduct, category3));
            if (category4String != null) genericProductCategories.add(new GenericProductCategory(genericProductCategoryId.incrementAndGet(), genericProduct, category4));

        });

        Map<Integer, Company> companies = new HashMap<>();
        products = new HashMap<>();

        productRows.forEach(productRow -> {

            /*
             * Populate companies
             */
            // add producer to companies
            Integer producerId = Integer.valueOf(productRow.getProducerId());
            Company producer = new Company(producerId, productRow.getProducerName(), productRow.getProducerAddress());
            companies.put(producerId, producer);

            // add license holder to companies
            // in the case where producerId = licenseHolderId, producer info will be taken
            Integer licenseHolderId = Integer.valueOf(productRow.getLicenseHolderId());
            Company licenseHolder = new Company(licenseHolderId, productRow.getLicenseHolderName(), productRow.getLicenseHolderAddress());
            companies.put(licenseHolderId, licenseHolder);

            /*
             * Populate products
             */
            String productId = productRow.getId();
            String genericName = productRow.getGenericName();
            GenericProduct genericProduct = genericProducts.get(genericName);
            String name = productRow.getName();
            Category category = categories.get(productRow.getPrimaryCategory());

            Product product = new Product(productId, genericProduct, name, category, producer, licenseHolder);
            products.put(productId, product);
        });

        //System.out.println("products: " + products.size());
        //System.out.println("genericProducts: " + genericProducts.size());

        return null;
        //throw new MSException(MSException.DEFAULT_MESSAGE);
    }

    /* MS Generic Names */
    /**
     * Method find the number of unique generic names.
     * 
     * @return
     */
    public Object numberOfUniqueGenericNames() {
        return genericProducts.size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     * 
     * @return
     */
    public Object numberOfDuplicateGenericNames() {
        return genericNames.size() - genericProducts.size();
    }

    /* MS Products */
    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     * 
     * @return
     */
    public Object numberOfMSProductsWithGenericName() {
        return products
                .values()
                .stream()
                .filter(product -> product.getGenericProduct() != null)
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     * 
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName() {
        return products
                .values()
                .stream()
                .filter(product -> product.getGenericProduct() == null)
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     * 
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {
        Map<Company, List<Product>> prods = products
                .values()
                .stream()
                .filter(product -> product.getProducer().getId().equals(product.getLicenseHolder().getId()))
                .collect(Collectors.groupingBy(Product::getProducer))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(l -> l.getValue().size()))
                .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (a, b) -> { throw new AssertionError();},
                                    LinkedHashMap::new));

        List<Map.Entry<Company, List<Product>>> list = new ArrayList<>(prods.entrySet());
        Map.Entry<Company, List<Product>> lastEntry = list.get(list.size()-1);

        //System.out.println("company: " + lastEntry.getKey().getName() + " size: " + lastEntry.getValue().size());

        return lastEntry.getKey().getName();
    }

    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     * 
     * @param companyName
     * @return
     */
    public Object numberOfMSProductsByProducerName(String companyName) {
        return products
                .values()
                .stream()
                .filter(product -> product.getProducer().getName().toLowerCase().startsWith(companyName))
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Method finds the products whose generic name has the category of interest.
     * 
     * @param category
     * @return
     */
    public Set<MSProductIdentity> findMSProductsWithGenericNameCategory(String category) {
        throw new MSException(MSException.DEFAULT_MESSAGE);
    }
}
