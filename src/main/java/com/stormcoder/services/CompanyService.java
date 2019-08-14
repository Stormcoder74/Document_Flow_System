package com.stormcoder.services;

import com.stormcoder.entities.Company;
import com.stormcoder.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getByName(String name){
        return companyRepository.findByName(name);
    }

    public List<Company> getAll() {
        return (List<Company>) companyRepository.findAll();
    }
}
