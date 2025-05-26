package com.job.services.impl;

import com.job.pojo.FollowNotice;
import com.job.pojo.Notification;
import com.job.pojo.UserNotification;
import com.job.repositories.FollowNoticeRepository;
import com.job.repositories.NotificationRepository;
import com.job.repositories.UserNotificationRepository;
import com.job.services.NotificationService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private UserNotificationRepository userNotificationRepo;

    @Autowired
    private FollowNoticeRepository followNoticeRepo;

    @Override
    public Notification createNotificationForFollowers(Notification n) {
        n.setCreatedAt(new Date());
        Notification saved = notificationRepo.addNotification(n);

        List<FollowNotice> followers = followNoticeRepo.getFollowersByEmployer(n.getEmployerId().getId());
        for (FollowNotice f : followers) {
            UserNotification u = new UserNotification();
            u.setEmployeeId(f.getEmployee());
            u.setNotificationId(saved);
            u.setIsRead(false);
            u.setReadTime(null);
            userNotificationRepo.addUserNotification(u);
        }
        return saved;
    }

    @Override
    public List<Notification> getNotificationsByEmployee(int employeeId) {
        return this.notificationRepo.getNotificationsByEmployeeId(employeeId);
    }
}
