package com.stormcoder.controllers;

import com.stormcoder.entities.Company;
import com.stormcoder.entities.Document;
import com.stormcoder.entities.authenticate.User;
import com.stormcoder.services.CompanyService;
import com.stormcoder.services.DocumentService;
import com.stormcoder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/documents")
public class DocumentController {
    private DocumentService documentService;
    private UserService userService;
    private CompanyService companyService;

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        Set<Document> documents = new HashSet<>(user.getCompany().getInputDocuments());
        documents.addAll(user.getCompany().getOutputDocuments());
        model.addAttribute("documents", documents);
        return "documents";
    }

    @GetMapping("/add-form")
    public String addDocument(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("firstCompany", user.getCompany().getName());

        List<String> companyNames = companyService.getAll()
                .stream()
                .map(Company::getName)
                .filter(companyName -> !(companyName.equals(user.getCompany().getName())))
                .collect(Collectors.toList());
        model.addAttribute("companies", companyNames);

        return "add-document";
    }

    @PostMapping("/add")
    public String addDocument(@ModelAttribute("title") String title,
                              @ModelAttribute("firstCompany") String firstCompany,
                              @ModelAttribute("secondCompany") String secondCompany,
                              @ModelAttribute("content") String content) {

        Document document = new Document(title, companyService.getByName(firstCompany),
                companyService.getByName(secondCompany), content);

        if (document.getTitle() != null && !document.getTitle().isEmpty() &&
                document.getFirstCompany() != null &&
                document.getSecondCompany() != null &&
                document.getContent() != null && !document.getContent().isEmpty()) {
            documentService.save(document);
        }

        return "redirect:/documents";
    }

    @GetMapping("/edit/{id}")
    public String editDocument(Model model, @PathVariable(value = "id") Long id) {
        Document document = documentService.getById(id);
        if (document != null) {
            model.addAttribute("document", document);
        }
        return "edit-document";
    }

    @PostMapping("/save")
    public String saveDocument(@ModelAttribute("document") Document savedDocument) {
        Document document = documentService.getById(savedDocument.getId());
        if (document != null) {
            document.setContent(savedDocument.getContent());

            Company tmpCompany = document.getFirstCompany();
            document.setFirstCompany(document.getSecondCompany());
            document.setSecondCompany(tmpCompany);

            document.setFirstSignature(false);

            documentService.save(document);
        }
        return "redirect:/documents";
    }

    @GetMapping("/sign/{id}")
    public String showOneProduct(Principal principal, @PathVariable("id") Long id) {
        User user = userService.findByUsername(principal.getName());
        Document document = documentService.getById(id);
        if (document != null) {
            if (document.getFirstCompany().getName().equals(user.getCompany().getName())) {
                document.setFirstSignature(true);
            } else if (document.getSecondCompany().getName().equals(user.getCompany().getName())
                    && document.isFirstSignature()) {
                document.setSecondSignature(true);
            }
            documentService.save(document);
        }
        return "redirect:/documents";
    }
}
