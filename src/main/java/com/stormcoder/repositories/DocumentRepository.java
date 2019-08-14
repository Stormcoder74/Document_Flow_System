package com.stormcoder.repositories;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    @Query(value = "SELECT COUNT(d) FROM Document d WHERE" +
            "((d.firstCompany = ?1 or d.secondCompany = ?1) and" +
            " (d.firstSignature = false or d.secondSignature = false))")
    int countAllIncompleteDocFlows(Company company);

    @Query(value = "SELECT COUNT(d) FROM Document d WHERE d.companyCreator = ?1 and d.creationTime > ?2")
    int countAllCreatedPerHour(Company company, Timestamp oneHourAgo);

    @Query(value = "SELECT COUNT(d) FROM Document d WHERE" +
            "(((d.firstCompany = ?1 and d.secondCompany = ?2)" +
            "or (d.firstCompany = ?2 and d.secondCompany = ?1)) and" +
            " (d.firstSignature = false or d.secondSignature = false))")
    int countIncompleteDocFlowsWithTheCompany(Company company1, Company company2);
}
