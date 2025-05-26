/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

/**
 *
 * @author User
 */
import com.job.pojo.Notification;
import java.util.List;

public interface NotificationService {
     Notification addUserNotification(Notification n);
    List<Notification> getNotificationsByEmployee(int employeeId);
}
