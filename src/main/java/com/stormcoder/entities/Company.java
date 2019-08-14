package com.stormcoder.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "firstCompany",
            fetch = FetchType.LAZY)
    private Set<Document> outputDocuments;

    @OneToMany(mappedBy = "secondCompany",
            fetch = FetchType.LAZY)
    private Set<Document> inputDocuments;

    public Company() {
        outputDocuments = new HashSet<>();
        inputDocuments = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id.equals(company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Document> getOutputDocuments() {
        return outputDocuments;
    }

    public Set<Document> getInputDocuments() {
        return inputDocuments;
    }
}
