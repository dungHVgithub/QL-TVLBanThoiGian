/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.job.pojo.CompanyImages;
import com.job.pojo.CompanyInformation;
import com.job.services.CompanyImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCompanyImagesController {
    @Autowired
    private CompanyImagesService companyImagesService;

    @GetMapping("/company_images")
    public ResponseEntity<List<CompanyImages>> list() {
        List<CompanyImages> companyImages = companyImagesService.getAllCompanyImages();
        return new ResponseEntity<>(companyImages, HttpStatus.OK);
    }

    @GetMapping("/company_images/{companyId}")
    public ResponseEntity<List<CompanyImages>> listByCompanyId(@PathVariable("companyId") int companyId) {
        List<CompanyImages> companyImages = companyImagesService.getCompanyImagesByCompanyId(companyId);
        return new ResponseEntity<>(companyImages, HttpStatus.OK);
    }

    @PostMapping(path = "/company_images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyImages> addCompanyImage(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            CompanyImages companyImage = new CompanyImages();
            companyImage.setCaption(params.get("caption"));
            companyImage.setUploadTime(params.get("uploadTime") != null ? new Date(Long.parseLong(params.get("uploadTime"))) : new Date());

            // Xử lý companyId
            int companyId = Integer.parseInt(params.get("companyId"));
            CompanyInformation companyInfo = new CompanyInformation();
            companyInfo.setId(companyId); // Giả sử chỉ cần set ID để liên kết
            companyImage.setCompanyId(companyInfo);

            // Xử lý file nếu có
            if (file != null && !file.isEmpty()) {
                Map res = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                companyImage.setImagePath(res.get("secure_url").toString());
            } else {
                companyImage.setImagePath(""); // Giá trị mặc định nếu không có file
            }

            companyImagesService.addCompanyImage(companyImage);
            return new ResponseEntity<>(companyImage, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Inject Cloudinary (đảm bảo đã cấu hình trong ứng dụng)
    @Autowired
    private Cloudinary cloudinary;
}