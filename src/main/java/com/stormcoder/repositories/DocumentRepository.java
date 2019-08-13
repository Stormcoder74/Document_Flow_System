package com.stormcoder.repositories;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    Iterable<Document> getAllByFirstCompanyAndSecondCompany(Company company1, Company company2);
    int countAllByFirstCompanyOrSecondCompanyAndFirstSignatureOrSecondSignature(
            Company company1, Company company2, boolean sign1, boolean sign2);
}
