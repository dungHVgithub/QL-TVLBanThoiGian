/*
 * Click nanofibers://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nanofibers://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.UserDocuments;
import com.job.repositories.UserDocumentsRepository;
import com.job.services.UserDocumentsService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DUNG
 */
@Service
@Transactional
public class UserDocumentsServiceImpl implements UserDocumentsService {

    @Autowired
    private UserDocumentsRepository udRepo;

    @Override
    public List<UserDocuments> getDocumentses() {
        return this.udRepo.getDocumentses();
    }

    @Override
    public void updateStatus(Integer id, String status, String updatedDate, String approvedBy) {
        UserDocuments document = udRepo.getDocumentses().stream()
                .filter(ud -> ud.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (document != null) {
         
            document.setStatus(status);

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date parsedDate = dateFormat.parse(updatedDate);
                document.setUpdatedDate(parsedDate);
            } catch (ParseException e) {
                document.setUpdatedDate(new Date()); // Fallback nếu không parse được
            }

           
            document.setApprovedByAdminId(null); // Xóa mối quan hệ cũ nếu có
          
            udRepo.updateStatus(document);
        }
    }
}