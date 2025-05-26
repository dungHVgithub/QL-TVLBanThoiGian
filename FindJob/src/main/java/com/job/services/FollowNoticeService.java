package com.job.services;

import com.job.dto.FollowNoticeDTOEmployee;
import com.job.dto.FollowNoticeDTOEmployer;
import com.job.pojo.FollowNotice;
import java.util.List;

public interface FollowNoticeService {
    FollowNotice  addOrUpdateFollow(FollowNotice follow);
    List<FollowNoticeDTOEmployee> getFollowDTOsByEmployee(int employeeId);
    long countFollowersByEmployer(int employerId);
    boolean deleteFollow(int employeeId, int employerId);
    boolean existsFollow(int employeeId, int employerId);
    FollowNotice getFollowDetail(int employeeId, int employerId);
    List<FollowNoticeDTOEmployer> getFollowerDTOsByEmployer(int employerId);
}