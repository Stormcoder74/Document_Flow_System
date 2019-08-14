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

        Set<Document> documents = user.getCompany().getInputDocuments();
        documents.addAll(user.getCompany().getOutputDocuments());
        model.addAttribute("documents", documents);

        return "documents";
    }

    @GetMapping("/add-form")
    public String addDocument(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (!documentService.canCreateDocument(user.getCompany())) {
            model.addAttribute("errorMessage", "Вы превышаете допустимое количество документооборотов!");
            return "error-page";
        }

        if (!documentService.canCreateDocumentPerHour(user.getCompany())) {
            model.addAttribute("errorMessage", "Вы превышаете допустимое количество созданных документов в час!");
            return "error-page";
        }

        model.addAttribute("firstCompany", user.getCompany().getName());

        List<String> companyNames = companyService.getAll().stream()
                .map(Company::getName)
                .filter(companyName -> !(companyName.equals(user.getCompany().getName())))
                .collect(Collectors.toList());
        model.addAttribute("companies", companyNames);

        return "add-document";
    }

    @PostMapping("/add")
    public String addDocument(Model model,
                              @ModelAttribute("title") String title,
                              @ModelAttribute("firstCompany") String firstCompany,
                              @ModelAttribute("secondCompany") String secondCompany,
                              @ModelAttribute("content") String content) {
        if (!documentService.canCreateDocumentWithTheCompany(
                companyService.getByName(firstCompany), companyService.getByName(secondCompany))) {
            model.addAttribute("errorMessage", "Вы превышаете " +
                    "допустимое количество документооборотов с одной компанией!");
            return "error-page";
        }

        Document document = new Document(title, companyService.getByName(firstCompany),
                companyService.getByName(firstCompany), companyService.getByName(secondCompany), content);
        try {
            documentService.save(document);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }

        return "redirect:/documents";
    }

    @GetMapping("/edit/{id}")
    public String editDocument(Model model, @PathVariable(value = "id") Long id) {
        if (!documentService.canEditAndSign()) {
            model.addAttribute("errorMessage", "В это время запрещено редактирование документа!");
            return "error-page";
        }

        Document document = documentService.getById(id);
        if (document == null) {
            model.addAttribute("errorMessage", "Нет такого документа!");
            return "error-page";
        }

        model.addAttribute("document", document);
        return "edit-document";
    }

    @PostMapping("/save")
    public String saveDocument(Model model, Principal principal,
                               @ModelAttribute("document") Document savedDocument) {
        Document document = documentService.getById(savedDocument.getId());
        if (document == null) {
            model.addAttribute("errorMessage", "Нет такого документа!");
            return "error-page";
        }

        document.setContent(savedDocument.getContent());

        if (document.getSecondCompany().equals(
                userService.findByUsername(principal.getName()).getCompany()
        )) {
            Company tmpCompany = document.getFirstCompany();
            document.setFirstCompany(document.getSecondCompany());
            document.setSecondCompany(tmpCompany);
        }

        document.setFirstSignature(false);

        try {
            documentService.save(document);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
        return "redirect:/documents";
    }

    @GetMapping("/sign/{id}")
    public String showOneProduct(Model model, Principal principal, @PathVariable("id") Long id) {
        if (!documentService.canEditAndSign()) {
            model.addAttribute("errorMessage", "В это время запрещено подписание документа!");
            return "error-page";
        }

        User user = userService.findByUsername(principal.getName());
        Document document = documentService.getById(id);
        if (document == null) {
            model.addAttribute("errorMessage", "Нет такого документа!");
            return "error-page";
        }

        documentService.sign(document, user);
        documentService.save(document);
        return "redirect:/documents";
    }

    @GetMapping("/delete/{id}")
    public String deleteDocument(Model model, Principal principal,
                                 @PathVariable(value = "id") Long id) {
        try {
            documentService.deleteById(id, userService.findByUsername(principal.getName()));
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
        return "redirect:/documents";
    }
}
