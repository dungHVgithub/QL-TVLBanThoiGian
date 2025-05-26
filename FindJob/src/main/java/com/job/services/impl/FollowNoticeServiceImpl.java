package com.job.services.impl;

import com.job.dto.FollowNoticeDTOEmployee;
import com.job.dto.FollowNoticeDTOEmployer;
import com.job.pojo.FollowNotice;
import com.job.repositories.FollowNoticeRepository;
import com.job.services.FollowNoticeService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowNoticeServiceImpl implements FollowNoticeService {

    @Autowired
    private FollowNoticeRepository followRepo;

    @Override
    public FollowNotice addOrUpdateFollow(FollowNotice follow) {
        return followRepo.addOrUpdateFollow(follow);
    }

    @Override
    public List<FollowNoticeDTOEmployee> getFollowDTOsByEmployee(int employeeId) {
        List<FollowNotice> follows = followRepo.getFollowsByEmployee(employeeId);
        return follows.stream().map(f -> new FollowNoticeDTOEmployee(
                f.getEmployer().getId(),
                f.getFollowTime()
        )).collect(Collectors.toList());
    }
     @Override
    public List<FollowNoticeDTOEmployer> getFollowerDTOsByEmployer(int employerId) {
         List<FollowNotice> follows = followRepo.getFollowersByEmployer(employerId);
        return follows.stream().map(f -> new FollowNoticeDTOEmployer(
                f.getEmployee().getId(),
                f.getFollowTime()
        )).collect(Collectors.toList());
    }

    @Override
    public long countFollowersByEmployer(int employerId) {
        return followRepo.countFollowersByEmployer(employerId);
    }

    @Override
    public boolean deleteFollow(int employeeId, int employerId) {
        return followRepo.deleteFollow(employeeId, employerId);
    }

    @Override
    public boolean existsFollow(int employeeId, int employerId) {
        return followRepo.existsFollow(employeeId, employerId);
    }

    @Override
    public FollowNotice getFollowDetail(int employeeId, int employerId) {
        return followRepo.getFollowDetail(employeeId, employerId);
    }

   
}
