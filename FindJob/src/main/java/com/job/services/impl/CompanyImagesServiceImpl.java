/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.CompanyImages;
import com.job.repositories.CompanyImagesRepository;
import com.job.services.CompanyImagesService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CompanyImagesServiceImpl implements CompanyImagesService {
    @Autowired
    private CompanyImagesRepository companyImagesRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<CompanyImages> getAllCompanyImages() {
        return companyImagesRepository.getAllCompanyImages();
    }

    @Override
    public List<CompanyImages> getCompanyImagesByCompanyId(int companyId) {
        return companyImagesRepository.getCompanyImagesByCompanyId(companyId);
    }

    @Override
public void addCompanyImage(CompanyImages companyImage) {
    if (companyImage.getFile() != null && !companyImage.getFile().isEmpty()) {
        try {
            Map res = cloudinary.uploader().upload(companyImage.getFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            companyImage.setImagePath(res.get("secure_url").toString());
        } catch (IOException ex) {
            Logger.getLogger(CompanyImagesServiceImpl.class.getName()).log(Level.SEVERE, "Error uploading to Cloudinary", ex);
            throw new RuntimeException("Failed to upload image to Cloudinary", ex);
        }
    } else if (companyImage.getImagePath() == null) {
        companyImage.setImagePath(""); // Giá trị mặc định nếu không có file
    }

    if (companyImage.getCompanyId() == null || companyImage.getCompanyId().getId() == null) {
        throw new IllegalArgumentException("companyId is required");
    }

    if (companyImage.getUploadTime() == null) {
        companyImage.setUploadTime(new Date());
    }

    companyImagesRepository.addCompanyImage(companyImage);
    }
}