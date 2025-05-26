package com.job.dto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DUNG
 */
import com.job.pojo.CompanyImages;


public class CompanyImageDTO {
    private Integer id;
    private String imagePath;
    private String caption;
    private Long uploadTime;
    private CompanyShortDTO companyId;

    public CompanyImageDTO(CompanyImages img) {
        this.id = img.getId();
        this.imagePath = img.getImagePath();
        this.caption = img.getCaption();
        this.uploadTime = img.getUploadTime() != null ? img.getUploadTime().getTime() : null;
        this.companyId = new CompanyShortDTO(img.getCompanyId());
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
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return the uploadTime
     */
    public Long getUploadTime() {
        return uploadTime;
    }

    /**
     * @param uploadTime the uploadTime to set
     */
    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * @return the companyId
     */
    public CompanyShortDTO getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(CompanyShortDTO companyId) {
        this.companyId = companyId;
    }

   
}
