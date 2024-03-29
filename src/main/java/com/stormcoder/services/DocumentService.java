package com.stormcoder.services;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import com.stormcoder.entities.DocumentsConstraints;
import com.stormcoder.entities.authenticate.User;
import com.stormcoder.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalTime;

@Service
public class DocumentService {
    private DocumentRepository documentRepository;
    private DocumentsConstraints documentsConstraints;

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Autowired
    public void setDocumentsConstraints(DocumentsConstraints documentsConstraints) {
        this.documentsConstraints = documentsConstraints;
    }

    public Document getById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public void save(Document document) throws IllegalArgumentException {
        if (document.getTitle() != null && !document.getTitle().isEmpty() &&
                document.getCompanyCreator() != null &&
                document.getFirstCompany() != null &&
                document.getSecondCompany() != null) {

            if (document.getCreationTime() == null) {
                document.setCreationTime(new Timestamp(System.currentTimeMillis()));
            }
            documentRepository.save(document);
        } else {
            throw new IllegalArgumentException("Некоторые параметры документа заданы неверно!");
        }
    }

    public void sign(Document document, User user) {
        if (document.getFirstCompany().equals(user.getCompany())) {
            document.setFirstSignature(true);
        } else if (document.getSecondCompany().equals(user.getCompany())
                && document.isFirstSignature()) {
            document.setSecondSignature(true);
        }
    }

    public void deleteById(Long id, User user) throws IllegalArgumentException {
        Document document = documentRepository.findById(id).orElse(null);
        if (document != null) {
            if (document.getCompanyCreator().equals(user.getCompany())) {
                if (!document.isFirstSignature() && !document.isSecondSignature()) {
                    documentRepository.deleteById(id);
                } else {
                    throw new IllegalArgumentException("Документ нельзя удалить, он уже подписан!");
                }
            } else {
                throw new IllegalArgumentException("Документ нельзя удалить, он создан другой компанией!");
            }
        } else {
            throw new IllegalArgumentException("Нет такого документа!");
        }
    }

    public boolean canEditAndSign() {
        return LocalTime.now().isAfter(documentsConstraints.getSignEditionTimeMin()) &&
                LocalTime.now().isBefore(documentsConstraints.getSignEditionTimeMax());
    }

    public boolean canCreateDocument(Company company) {
        int count = documentRepository.countAllIncompleteDocFlows(company);
        return count < documentsConstraints.getMaxDocumentFlows();
    }

    public boolean canCreateDocumentPerHour(Company company) {
        Timestamp oneHourAgo = new Timestamp(System.currentTimeMillis() - 3600000);
        int count = documentRepository.countAllCreatedPerHour(company, oneHourAgo);
        return count < documentsConstraints.getMaxDocumentPerHour();
    }

    public boolean canCreateDocumentWithTheCompany(Company company1, Company company2) {
        int count = documentRepository.countIncompleteDocFlowsWithTheCompany(company1, company2);
        return count < documentsConstraints.getMaxDocumentFlowsPerCompany();
    }
}
