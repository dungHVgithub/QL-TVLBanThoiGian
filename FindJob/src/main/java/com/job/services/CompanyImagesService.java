/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import com.job.pojo.CompanyImages;

import java.util.List;

public interface CompanyImagesService {

    List<CompanyImages> getAllCompanyImages();

    List<CompanyImages> getCompanyImagesByCompanyId(int companyId);

    void addCompanyImage(CompanyImages companyImage);

    CompanyImages getCompanyImageById(int id);

    void updateCompanyImage(CompanyImages companyImage);
}
