package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

import java.util.Objects;

public class Company {

    private Integer id;
    private String name;
    private String address;

    public Company(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return id.equals(company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
