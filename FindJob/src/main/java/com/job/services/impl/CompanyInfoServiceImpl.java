/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.CompanyInformation;
import com.job.repositories.CompanyInfoRepository;
import com.job.services.CompanyInfoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DUNG
 */
@Service
public class CompanyInfoServiceImpl implements CompanyInfoService{
    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Override
    public List<CompanyInformation> companyInformations(Map<String, String> params) {
       return this.companyInfoRepository.companyInformations(params);
    }

    @Override
    public CompanyInformation companyInformationById(int id) {
        return this.companyInfoRepository.companyInformationById(id);
    }

    @Override
    public CompanyInformation addOrUpdate(CompanyInformation ci) {
        return this.companyInfoRepository.addOrUpdate(ci);
         }

    @Override
    public void deleteCompanyInformation(int id) {
           this.companyInfoRepository.deleteCompanyInformation(id);
    }
    
}
