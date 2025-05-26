/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import com.job.pojo.UserNotification;
import java.util.List;

/**
 *
 * @author DUNG
 */
public interface UserNotificationService {
     List<UserNotification> getNotificationsByEmployeeId(int employeeId);
    UserNotification addUserNotification(UserNotification u);
    boolean markNotificationAsRead(int notificationId, int employeeId, boolean isRead);
   long countUnreadByEmployeeId(int employeeId);

}
