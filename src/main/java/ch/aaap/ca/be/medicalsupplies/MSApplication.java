package ch.aaap.ca.be.medicalsupplies;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductIdentity;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.Category;
import ch.aaap.ca.be.medicalsupplies.model.GenericProduct;
import ch.aaap.ca.be.medicalsupplies.model.GenericProductCategory;
import ch.aaap.ca.be.medicalsupplies.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;

    private static Map<String, GenericProduct> genericProducts;
    private static Map<String, Product> products;
    private static Map<GenericProduct, GenericProductCategory> genericProductCategories;

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
        genericProductCategories = new HashMap<>();

        genericNameRows.forEach(genericNameRow -> {
            /*
             * populate categories
             */
            List<String> categoryStrings = Arrays.asList(genericNameRow.getCategory1(), genericNameRow.getCategory2(), genericNameRow.getCategory3(), genericNameRow.getCategory4());
            Set<Category> categoriesSet = new HashSet<>();
            categoryStrings.forEach(categoryString -> {
                if (categoryString != null && !categoryString.isEmpty()) {
                    Category category =new Category(categoryString);
                    categories.put(categoryString, category);
                    categoriesSet.add(category);
                }
            });

            /*
             * populate genericProductCategory
             */
            GenericProductCategory genericProductCategory = new GenericProductCategory(genericNameRow, categoriesSet);
            GenericProduct genericProduct = genericProductCategory.getGenericProduct();
            genericProductCategories.put(genericProduct, genericProductCategory);

            /*
             * populate genericProducts
             */
            genericProducts.put(genericNameRow.getName(), genericProduct);

        });

        products = new HashMap<>();

        productRows.forEach(productRow -> {
            /*
             * Populate products
             */
            products.put(productRow.getId(), new Product(productRow, genericProducts, categories));
        });

        return products;
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
        return products
                .values()
                .stream()
                .filter(product -> product.getProducer().getId().equals(product.getLicenseHolder().getId()))
                .collect(Collectors.groupingBy(Product::getProducer))
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(l -> l.getValue().size()))
                .orElse(null)
                .getKey()
                .getName();
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
        return products
                .values()
                .stream()
                .filter(product -> genericProductCategories.get(product.getGenericProduct()) != null
                                    && genericProductCategories.get(product.getGenericProduct()).getCategories().contains(new Category(category)))
                .collect(Collectors.toSet());
    }
}
