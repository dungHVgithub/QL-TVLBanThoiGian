/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.pojo.Employer;
import com.job.pojo.Notification;
import com.job.services.EmployerService;
import com.job.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DUNG
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiNotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmployerService empservice;

    @PostMapping("/addNotifications")// gửi thông báo cho tất cả follower của employer
    public ResponseEntity<Notification> addNotificationByEmployer(
            @RequestParam("content") String content,
            @RequestParam("employerId") int employerId) {
        Employer emp = empservice.getEmployerById(employerId);
        Notification n = new Notification();
        n.setContent(content);
        n.setEmployerId(emp); // Đây là Employer object
        Notification created = notificationService.addUserNotification(n);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
}
