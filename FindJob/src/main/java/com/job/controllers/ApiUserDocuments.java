/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.services.UserDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 *
 * @author DUNG
 */
@Controller
@RequestMapping("/user_documents")
public class ApiUserDocuments {

    @Autowired
    private UserDocumentsService udService;

    @GetMapping
    public String UdView(Model model) {
        model.addAttribute("uds", udService.getDocumentses());
        return "userdocuments";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestBody Map<String, Object> payload, Model model) {
        Integer id = Integer.parseInt(payload.get("id").toString());
        String status = payload.get("status").toString();
        String updatedDate = payload.get("updatedDate").toString();
        String approvedBy = payload.get("approvedBy").toString();

        udService.updateStatus(id, status, updatedDate, approvedBy);
        model.addAttribute("uds", udService.getDocumentses()); // Cập nhật danh sách sau khi lưu
        return "userdocuments"; // Reload trang để hiển thị thay đổi
    }
}