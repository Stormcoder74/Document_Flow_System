package com.stormcoder.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "firstCompany",
            fetch = FetchType.EAGER)
    private Set<Document> outputDocuments;

    @OneToMany(mappedBy = "secondCompany",
            fetch = FetchType.EAGER)
    private Set<Document> inputDocuments;

    public Company(String name) {
        this.name = name;
        outputDocuments = new HashSet<>();
        inputDocuments = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Document> getOutputDocuments() {
        return new HashSet<>(outputDocuments);
    }

    public void addOutputDocument(Document outputDocument) {
        outputDocuments.add(outputDocument);
    }

    public Set<Document> getInputDocuments() {
        return new HashSet<>(inputDocuments);
    }

    public void addInputDocument(Document inputDocument) {
        inputDocuments.add(inputDocument);
    }
}
