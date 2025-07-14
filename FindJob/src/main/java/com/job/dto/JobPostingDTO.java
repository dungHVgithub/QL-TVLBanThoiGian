package com.job.dto;

import java.util.Date;

public class JobPostingDTO {

    private Integer id;
    private String name;
    private Double salary;
    private String timeStart; // Nhận chuỗi "HH:mm"
    private String timeEnd; // Nhận chuỗi "HH:mm"
    private Integer categoryId; // Số nguyên
    private EmployerDTO employer; // Thêm trường EmployerDTO
    private String state;

    // Constructor
    public JobPostingDTO() {
    }

    public JobPostingDTO(Integer id,  String name, Double salary, String timeStart, String timeEnd,
            Integer categoryId, EmployerDTO employer, String state) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.categoryId = categoryId;
        this.employer = employer;
        this.state = state;
    }

    // Getters và Setters
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public EmployerDTO getEmployer() {
        return employer;
    }

    public void setEmployer(EmployerDTO employer) {
        this.employer = employer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
