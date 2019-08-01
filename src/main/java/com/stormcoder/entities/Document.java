package com.stormcoder.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "documents")
public class Document implements Singable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

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

    public Document(String title, Company firstCompany,
                    Company secondCompany, String content) {
        this.title = title;
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
        return id == document.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean signIt(Company company) {
        if (secondCompany.equals(company) && !secondSignature){
            secondSignature = true;
            Company tmpCompany = firstCompany;
            firstCompany = secondCompany;
            secondCompany = tmpCompany;
            return true;
        }
        return false;
    }
}
