/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.pojo.UserNotification;
import com.job.services.UserNotificationService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
public class ApiUserNotificationController {

    @Autowired
    private UserNotificationService userNotificationService;

    @GetMapping("/notifications/by_employee/{employeeId}")
    public ResponseEntity<List<UserNotification>> getNotificationsByEmployee(@PathVariable("employeeId") int employeeId) {
        List<UserNotification> list = userNotificationService.getNotificationsByEmployeeId(employeeId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/notifications/read")// cập nhật khi người dùng nhấn vào thông báo -> đọc
    public ResponseEntity<?> markAsRead(
            @RequestParam("notificationId") int notificationId,
            @RequestParam("employeeId") int employeeId,
            @RequestParam("isRead") boolean isRead
    ) {
        boolean updated = userNotificationService.markNotificationAsRead(notificationId, employeeId, isRead);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/notifications/unread_count/{employeeId}")
public ResponseEntity<Long> countUnreadNotifications(
    @PathVariable("employeeId") int employeeId
) {
    long count = userNotificationService.countUnreadByEmployeeId(employeeId);
    return ResponseEntity.ok(count);
}
}
