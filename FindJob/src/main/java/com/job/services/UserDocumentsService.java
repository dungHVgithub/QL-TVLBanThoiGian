/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nanofibers://netbeans/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import com.job.pojo.UserDocuments;
import java.util.List;

public interface UserDocumentsService {
    List<UserDocuments> getDocumentses();
    void updateStatus(Integer id, String status, String updatedDate, String approvedBy);
}