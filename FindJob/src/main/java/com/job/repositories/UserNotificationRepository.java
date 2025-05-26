/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

/**
 *
 * @author User
 */

import com.job.pojo.UserNotification;
import java.util.List;

public interface UserNotificationRepository {
    UserNotification addUserNotification(UserNotification un);
    List<UserNotification> getNotificationsByEmployeeId(int employeeId);
    long countUnreadNotifications(int employeeId);
    void markAsRead(int userNotificationId);
}