/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.job.formatter.CCCDParser;
import com.job.pojo.JobPosting;
import com.job.pojo.User;
import com.job.pojo.UserDocuments;
import com.job.services.JobPostingService;
import com.job.services.UserDocService;
import com.job.services.UserService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DUNG
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserDocController {

    @Autowired
    private UserDocService userDocService;

    @Autowired
    private UserService userService;

    @Autowired
    private Cloudinary cloudinary;

    @DeleteMapping("/user_documents/{userDocumentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "userDocumentId") int id) {
        this.userDocService.deleteUserDocs(id);
    }

    @GetMapping("/user_documents")
    public ResponseEntity<List<UserDocuments>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.userDocService.getUserDocs(params), HttpStatus.OK);
    }

    @GetMapping("/user_documents/{userDocumentId}")
    public ResponseEntity<UserDocuments> retrieve(@PathVariable(value = "userDocumentId") int id) {
        return new ResponseEntity<>(this.userDocService.getUserDocsById(id), HttpStatus.OK);
    }

    @PostMapping("/user_documents/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDocuments> create(@RequestBody UserDocuments userDocument) {
        UserDocuments savedDocument = this.userDocService.addOrUpdate(userDocument);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }

//    @PostMapping(
//            path = "/user_documents/ocr/cccd",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<?> extractAndUpdateFromCCCD(
//            @RequestPart("file") MultipartFile file,
//            @RequestPart("userId") String userIdStr) {
//
//        System.out.println("=== Enter OCR endpoint ===");
//        System.out.println("file==null? " + (file == null));
//        System.out.println("userIdStr=" + userIdStr);
//
//        try {
//            // Chuyển đổi userId từ String sang int
//            int userId = Integer.parseInt(userIdStr);
//
//            // Kiểm tra sự tồn tại của người dùng
//            User user = userService.getUserById(userId);
//            if (user == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(Map.of("error", "User with ID " + userId + " not found."));
//            }
//
//            // Đọc bytes từ file một lần
//            byte[] fileBytes = file.getBytes();
//
//            // Trích xuất văn bản từ ảnh
//            String ocrText = userDocService.extractTextFromImage(fileBytes);
//
//            // Phân tích thông tin từ văn bản OCR
//            Map<String, String> parsedInfo = CCCDParser.parse(ocrText);
//
//            // Tải ảnh lên Cloudinary
//            Map<?, ?> uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
//            String imageUrl = (String) uploadResult.get("secure_url");
//
//            // Tạo đối tượng UserDocuments mới
//            UserDocuments doc = new UserDocuments();
//            doc.setId(null); // auto-increment
//            doc.setUserid(user);
//            doc.setCreatedDate(new Date());
//            doc.setDocumentType("ID");
//            doc.setDocumentPath(imageUrl);
//            doc.setStatus("pending");
//
//            userDocService.addOrUpdate(doc);
//
//            // Cập nhật thông tin người dùng
//            if (parsedInfo.containsKey("name")) {
//                user.setName(parsedInfo.get("name"));
//            }
//            if (parsedInfo.containsKey("address")) {
//                user.setAddress(parsedInfo.get("address"));
//            }
//            if (parsedInfo.containsKey("birthday")) {
//                String[] parts = parsedInfo.get("birthday").split("/");
//                if (parts.length == 3) {
//                    String isoDate = parts[2] + "-" + parts[1] + "-" + parts[0];
//                    user.setBirthday(java.sql.Date.valueOf(isoDate));
//                }
//            }
//
//            userService.addUpdateUser(user);
//
//            // Trả kết quả
//            return ResponseEntity.ok(Map.of(
//                    "documentUrl", imageUrl,
//                    "ocrText", ocrText,
//                    "parsedFields", parsedInfo
//            ));
//
//        } catch (NumberFormatException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Invalid userId format."));
//        } catch (Exception e) {
//            e.printStackTrace(); // In ra lỗi để debug
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", e.getMessage()));
//        }
//    }

}
