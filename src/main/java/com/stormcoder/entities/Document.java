package com.stormcoder.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    private String title;

    @ManyToOne
    @JoinColumn(name = "company_creator")
    private Company companyCreator;

    @ManyToOne
    @JoinColumn(name = "first_company")
    private Company firstCompany;

    @ManyToOne
    @JoinColumn(name = "second_company")
    private Company secondCompany;

    @Column(name = "first_signature")
    private boolean firstSignature;

    @Column(name = "second_signature")
    private boolean secondSignature;

    private String content;

    public Document() {
    }

    public Document(String title, Company companyCreator, Company firstCompany,
                    Company secondCompany, String content) {
        this.title = title;
        this.companyCreator = companyCreator;
        this.firstCompany = firstCompany;
        this.secondCompany = secondCompany;
        firstSignature = false;
        secondSignature = false;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id.equals(document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getTitle() {
        return title;
    }

    public Company getCompanyCreator() {
        return companyCreator;
    }

    public Company getFirstCompany() {
        return firstCompany;
    }

    public void setFirstCompany(Company firstCompany) {
        this.firstCompany = firstCompany;
    }

    public Company getSecondCompany() {
        return secondCompany;
    }

    public void setSecondCompany(Company secondCompany) {
        this.secondCompany = secondCompany;
    }

    public boolean isFirstSignature() {
        return firstSignature;
    }

    public boolean isSecondSignature() {
        return secondSignature;
    }

    public void setFirstSignature(boolean firstSignature) {
        this.firstSignature = firstSignature;
    }

    public void setSecondSignature(boolean secondSignature) {
        this.secondSignature = secondSignature;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
