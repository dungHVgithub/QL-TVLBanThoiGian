/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.UserDocuments;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DUNG
 */
public interface UserDocRepository {

    List<UserDocuments> getUserDocs(Map<String, String> params);

    UserDocuments getUserDocsById(int id);

    UserDocuments addOrUpdate(UserDocuments j);

    void deleteUserDocs(int id);

    String extractTextFromImage(byte[] fileBytes) throws Exception;

}
