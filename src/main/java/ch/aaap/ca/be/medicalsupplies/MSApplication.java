package ch.aaap.ca.be.medicalsupplies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.*;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;

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
        Map<String, GenericProduct> genericProducts = new HashMap<>();
        final AtomicInteger genericProductCategoryId = new AtomicInteger(0);
        Set<GenericProductCategory> genericProductCategories = new HashSet<>();

        genericNameRows.forEach(genericNameRow -> {

            // get categories
            String category1String = (genericNameRow.getCategory1() == null || genericNameRow.getCategory1().isEmpty()) ? null : genericNameRow.getCategory1();
            String category2String = (genericNameRow.getCategory2() == null || genericNameRow.getCategory2().isEmpty()) ? null : genericNameRow.getCategory2();
            String category3String = (genericNameRow.getCategory3() == null || genericNameRow.getCategory3().isEmpty()) ? null : genericNameRow.getCategory3();;
            String category4String = (genericNameRow.getCategory4() == null || genericNameRow.getCategory4().isEmpty()) ? null : genericNameRow.getCategory4();

            System.out.println(genericNameRow.getId());
            System.out.println("category4String: " + "'" + category4String + "'");

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

        System.out.println("genericProducts: " + genericProducts.size());

        Map<Integer, Company> companies = new HashMap<>();
        Map<String, Product> products = new HashMap<>();

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
            companies.put(producerId, producer);

            /*
             * Populate products
             */
            String productId = productRow.getId();
            // if generic product does not exist in genericProducts add it
            String genericName = productRow.getGenericName();
            GenericProduct genericProduct = genericProducts.get(genericName) == null ? new GenericProduct(genericName) : genericProducts.get(genericName);
            genericProducts.put(genericName, genericProduct);
            String name = productRow.getName();
            // if category does not exist in categories add it
            String categoryString = (productRow.getPrimaryCategory() == null || productRow.getPrimaryCategory().isEmpty()) ? null : productRow.getPrimaryCategory();
            Category category = categories.get(categoryString) == null ? new Category(categoryString) : categories.get(categoryString);
            categories.put(categoryString, category);

            Product product = new Product(productId, genericProduct, name, category, producer, licenseHolder);
            products.put(productId, product);
        });

        System.out.println("products: " + products.size());

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
        throw new MSException(MSException.DEFAULT_MESSAGE);
    }

    /**
     * Method finds the number of generic names which are duplicated.
     * 
     * @return
     */
    public Object numberOfDuplicateGenericNames() {
        throw new MSException(MSException.DEFAULT_MESSAGE);
    }

    /* MS Products */
    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     * 
     * @return
     */
    public Object numberOfMSProductsWithGenericName() {
        throw new MSException(MSException.DEFAULT_MESSAGE);
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     * 
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName() {
        throw new MSException(MSException.DEFAULT_MESSAGE);
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     * 
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {
        throw new MSException(MSException.DEFAULT_MESSAGE);
    }

    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     * 
     * @param companyName
     * @return
     */
    public Object numberOfMSProductsByProducerName(String companyName) {
        throw new MSException(MSException.DEFAULT_MESSAGE);
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
