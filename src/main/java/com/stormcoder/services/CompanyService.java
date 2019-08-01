package com.stormcoder.services;

import com.stormcoder.entities.Company;
import com.stormcoder.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public Company getByName(String name){
        return companyRepository.findByName(name);
    }
}
