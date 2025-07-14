/*
 * Click nbfs://.netbeans.org/templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.job.dto.CompanyImageDTO;
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
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    @GetMapping("/company_images/{companyId}")
    public ResponseEntity<List<CompanyImageDTO>> listByCompanyId(@PathVariable("companyId") int companyId) {
        List<CompanyImages> images = companyImagesService.getCompanyImagesByCompanyId(companyId);
        if (images == null || images.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Trả về 404 nếu không có dữ liệu
        }
        List<CompanyImageDTO> result = images.stream()
                .map(CompanyImageDTO::new)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @PutMapping(path = "/company_images/{companyId}/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyImages> updateCompanyImage(
            @PathVariable("companyId") int companyId,
            @PathVariable("id") int id,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Lấy ảnh cần sửa
            CompanyImages existingImage = companyImagesService.getCompanyImageById(id);

            // Kiểm tra ID hợp lệ
            if (existingImage == null || existingImage.getCompanyId().getId() != companyId) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Cập nhật caption nếu có
            if (params.containsKey("caption")) {
                existingImage.setCaption(params.get("caption"));
            }

            // Cập nhật thời gian nếu có
            if (params.containsKey("uploadTime")) {
                existingImage.setUploadTime(new Date(Long.parseLong(params.get("uploadTime"))));
            }

            // Cập nhật ảnh nếu có
            if (file != null && !file.isEmpty()) {
                Map res = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                existingImage.setImagePath(res.get("secure_url").toString());
            }
            companyImagesService.updateCompanyImage(existingImage);

            return new ResponseEntity<>(existingImage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/company_images/{companyId}/{id}")
    public ResponseEntity<Void> deleteCompanyImage(
            @PathVariable("companyId") int companyId,
            @PathVariable("id") int id) {
        try {
            CompanyImages companyImage = companyImagesService.getCompanyImageById(id);
            if (companyImage == null || companyImage.getCompanyId().getId() != companyId) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            companyImagesService.deleteCompanyImage(id); // Gọi method xóa thực sự
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private Cloudinary cloudinary;
}
