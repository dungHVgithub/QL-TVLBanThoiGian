/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.Notification;
import java.util.List;

/**
 *
 * @author User
 */
public interface NotificationRepository {

    Notification addNotification(Notification n);
    List<Notification> getNotificationsByEmployeeId(int employeeId);
}
