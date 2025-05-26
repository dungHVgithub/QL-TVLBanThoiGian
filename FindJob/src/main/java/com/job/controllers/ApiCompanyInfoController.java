/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans.org/templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.dto.CompanyShortDTO;
import com.job.pojo.CompanyInformation;
import com.job.services.CompanyInfoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DUNG
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCompanyInfoController {
    @Autowired
    private CompanyInfoService companyInfoService;

    @DeleteMapping("/company_info/{company_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "company_id") int id) {
        this.companyInfoService.deleteCompanyInformation(id);
    }

    @GetMapping("/company_info")
    public ResponseEntity<List<CompanyInformation>> list(@RequestParam Map<String, String> params) {
        System.out.println(">> SIZE = " + this.companyInfoService.companyInformations(params).size());
        return new ResponseEntity<>(this.companyInfoService.companyInformations(params), HttpStatus.OK);
    }

    @GetMapping("/company_info/{company_id}")
public ResponseEntity<CompanyShortDTO> retrieve(@PathVariable("company_id") int id) {
    CompanyInformation company = companyInfoService.companyInformationById(id);
    if (company == null)
        return ResponseEntity.notFound().build();

    CompanyShortDTO dto = new CompanyShortDTO(company);
    return ResponseEntity.ok(dto);
}
    @PostMapping("/company_info")
    public ResponseEntity<CompanyInformation> create(@RequestBody CompanyInformation companyInfo) {
        try {
            CompanyInformation newCompanyInfo = companyInfoService.addOrUpdate(companyInfo);
            return new ResponseEntity<>(newCompanyInfo, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/company_info/{company_id}")
    public ResponseEntity<CompanyInformation> update(
            @PathVariable(value = "company_id") int id,
            @RequestBody CompanyInformation companyInfo) {
        try {
            CompanyInformation existingCompanyInfo = companyInfoService.companyInformationById(id);
            if (existingCompanyInfo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            companyInfo.setId(id); // Đảm bảo ID không thay đổi
            CompanyInformation updatedCompanyInfo = companyInfoService.addOrUpdate(companyInfo);
            return new ResponseEntity<>(updatedCompanyInfo, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}