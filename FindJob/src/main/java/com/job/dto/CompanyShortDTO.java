package com.job.dto;

import com.job.pojo.CompanyInformation;
import com.job.pojo.Employer;

public class CompanyShortDTO {
    private Integer id;
    private String name;
    private String address;
    private String taxCode;
    private Integer userId;

    public CompanyShortDTO(CompanyInformation company) {
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        this.taxCode = company.getTaxCode();

        if (company.getEmployerSet() != null && !company.getEmployerSet().isEmpty()) {
            Employer emp = company.getEmployerSet().iterator().next();
            if (emp.getUserId() != null)
                this.userId = emp.getUserId().getId();
        }
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the taxCode
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * @param taxCode the taxCode to set
     */
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    
}
