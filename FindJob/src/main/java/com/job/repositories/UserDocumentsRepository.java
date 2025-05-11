/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.UserDocuments;
import java.util.List;

/**
 *
 * @author DUNG
 */
public interface UserDocumentsRepository {
    List<UserDocuments> getDocumentses();
    void updateStatus(UserDocuments document);
}