package ch.aaap.ca.be.medicalsupplies.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MSGenericNameRow {

    private final Long id;
    private final String name;
    private final String category1;
    private final String category2;
    private final String category3;
    private final String category4;

    public MSGenericNameRow(Long id, String name, String category1, String category2, String category3,
                            String category4) {
        this.id = id;
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.category4 = category4;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public String getCategory4() {
        return category4;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public ArrayList<String> getCategories()
    {
        return new ArrayList<>(Arrays.asList(getCategory1(), getCategory2(), getCategory3(), getCategory4()));
    }

    public boolean hasCategory(final String category) {

        if (category == null || category.isEmpty()) {
            return false;
        }

        return (Objects.equals(category, getCategory1()) || Objects.equals(category, getCategory2()) || Objects.equals(category, getCategory3()) || Objects.equals(category, getCategory4()));

    }
}
