package com.stormcoder.controllers;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import com.stormcoder.services.CompanyService;
import com.stormcoder.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(path = "/documents")
public class DocumentController {
    DocumentService documentService;
    CompanyService companyService;

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String companyName){
        Company company = companyService.getByName(companyName);
        Set<Document> documents = new HashSet<>(company.getInputDocuments());
        documents.addAll(company.getOutputDocuments());
        model.addAttribute("documents", documents);
        return "documents";
    }

    @GetMapping("/edit/{id}")
    public String editDocument(Model model, @PathVariable(value = "id") Long id) {
        model.addAttribute("document", documentService.getById(id));
        return "edit-document";
    }

    @PostMapping("/save")
    public String saveDocument(@ModelAttribute("document") Document editedDocument) {
        Document document = documentService.getById(editedDocument.getId());
        document.setContent(editedDocument.getContent());
        documentService.save(document);
        return "redirect:/documents?company=Abibas";
    }

    @GetMapping("/sign/{id}")
    public String showOneProduct(Model model, @PathVariable(value = "id") Long id) {
        Document document = documentService.getById(id);
        if (document != null) {
            document.signIt(companyService.getByName("Abibas"));
        }
        return "redirect:/documents?company=Abibas";
    }
}
