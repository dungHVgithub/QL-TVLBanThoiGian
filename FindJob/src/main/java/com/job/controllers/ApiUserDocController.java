/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.cloudinary.Cloudinary;
import com.job.pojo.UserDocuments;
import com.job.services.UserDocService;
import com.job.services.UserService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/user_documents/by_user/{userId}")
    public ResponseEntity<List<UserDocuments>> getDocumentsByUserId(@PathVariable("userId") int userId) {
        List<UserDocuments> docs = this.userDocService.getDocsByUserId(userId);
        return new ResponseEntity<>(docs, HttpStatus.OK);
    }

    @PutMapping("/user_documents/{id}")
    public ResponseEntity<UserDocuments> updateUserDocument(
            @PathVariable("id") int id,
            @RequestBody UserDocuments updatedDoc) {
        try {
            UserDocuments existing = this.userDocService.getUserDocsById(id);
            
            if (existing == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Cập nhật các trường cần thiết
            existing.setName(updatedDoc.getName());
            existing.setDocumentType(updatedDoc.getDocumentType());
            existing.setUpdatedDate(new java.util.Date());

            UserDocuments saved = this.userDocService.addOrUpdate(existing);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}
