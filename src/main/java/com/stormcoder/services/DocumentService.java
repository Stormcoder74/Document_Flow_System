package com.stormcoder.services;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import com.stormcoder.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private DocumentRepository documentRepository;

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Iterable<Document> getAll() {
        return documentRepository.findAll();
    }

    public Iterable<Document> getAllByCompany(Company company) {
        return documentRepository.getAllByFirstCompanyAndSecondCompany(company, company);
    }

    public Document getById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public void save(Document document) throws IllegalArgumentException {
        if (document.getTitle() != null && !document.getTitle().isEmpty() &&
                document.getFirstCompany() != null &&
                document.getSecondCompany() != null &&
                document.getContent() != null && !document.getContent().isEmpty()) {
            documentRepository.save(document);
        } else {
            throw new IllegalArgumentException("Некоторые параметры документа заданы неверно!");
        }
    }
}
