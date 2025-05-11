/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.CompanyInformation;
import com.job.pojo.JobPosting;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DUNG
 */
public interface CompanyInfoRepository {

    List<CompanyInformation> companyInformations(Map<String, String> params);
    CompanyInformation companyInformationById(int id); 
    CompanyInformation addOrUpdate(CompanyInformation ci);
    void deleteCompanyInformation(int id);
}
