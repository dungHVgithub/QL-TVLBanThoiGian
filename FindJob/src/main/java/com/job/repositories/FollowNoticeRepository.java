/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.FollowNotice;
import java.util.List;

public interface FollowNoticeRepository {
    FollowNotice  addOrUpdateFollow(FollowNotice follow);
    List<FollowNotice> getFollowsByEmployee(int employeeId);
    List<FollowNotice> getFollowersByEmployer(int employerId);
    long countFollowersByEmployer(int employerId);
    boolean deleteFollow(int employeeId, int employerId);
    boolean existsFollow(int employeeId, int employerId);
    FollowNotice getFollowDetail(int employeeId, int employerId);
}

