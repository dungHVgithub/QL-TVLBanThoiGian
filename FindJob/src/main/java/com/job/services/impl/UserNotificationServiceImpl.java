/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.UserNotification;
import com.job.repositories.UserNotificationRepository;
import com.job.services.UserNotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DUNG
 */
@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Override
    public List<UserNotification> getNotificationsByEmployeeId(int employeeId) {
        return userNotificationRepository.getNotificationsByEmployeeId(employeeId);
    }

    @Override
    public UserNotification addUserNotification(UserNotification u) {
        return userNotificationRepository.addUserNotification(u);
    }

    @Override
    public boolean markNotificationAsRead(int notificationId, int employeeId, boolean isRead) {
        return this.userNotificationRepository.updateIsRead(notificationId, employeeId, isRead);
    }

    @Override
    public long countUnreadByEmployeeId(int employeeId) {
        return userNotificationRepository.countUnreadByEmployeeId(employeeId);
    }
}
