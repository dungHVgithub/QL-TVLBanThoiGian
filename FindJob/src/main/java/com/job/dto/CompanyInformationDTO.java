package com.job.dto;

public class CompanyInformationDTO {
    private Integer id;
    private String name;
    private String address;
    private String taxCode;

    public CompanyInformationDTO() {}

    public CompanyInformationDTO(Integer id, String name, String address, String taxCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.taxCode = taxCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}