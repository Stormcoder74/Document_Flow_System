package com.stormcoder.repositories;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    Iterable<Document> getAllByFirstCompanyAndSecondCompany(Company company1, Company company2);

    @Query(value = "SELECT COUNT(d) FROM Document d WHERE" +
            "((d.firstCompany = ?1 or d.secondCompany = ?1) and" +
            " (d.firstSignature = false or d.secondSignature = false))")
    int findAllIncompleteDocFlows(Company company);
}
