package com.job.dto;

public class EmployerDTO {
    private Integer id;
    private CompanyInformationDTO company;

    public EmployerDTO() {}

    public EmployerDTO(Integer id, CompanyInformationDTO company) {
        this.id = id;
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CompanyInformationDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyInformationDTO company) {
        this.company = company;
    }
}