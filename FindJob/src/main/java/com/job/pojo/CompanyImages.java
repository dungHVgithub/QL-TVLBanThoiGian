/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DUNG
 */
@Entity
@Table(name = "company_images")
@NamedQueries({
    @NamedQuery(name = "CompanyImages.findAll", query = "SELECT c FROM CompanyImages c"),
    @NamedQuery(name = "CompanyImages.findById", query = "SELECT c FROM CompanyImages c WHERE c.id = :id"),
    @NamedQuery(name = "CompanyImages.findByImagePath", query = "SELECT c FROM CompanyImages c WHERE c.imagePath = :imagePath"),
    @NamedQuery(name = "CompanyImages.findByCaption", query = "SELECT c FROM CompanyImages c WHERE c.caption = :caption"),
    @NamedQuery(name = "CompanyImages.findByUploadTime", query = "SELECT c FROM CompanyImages c WHERE c.uploadTime = :uploadTime")})
public class CompanyImages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "image_path")
    private String imagePath;
    @Size(max = 255)
    @Column(name = "caption")
    private String caption;
    @Column(name = "upload_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CompanyInformation companyId;
    @Transient
    private MultipartFile file;

    public CompanyImages() {
    }

    public CompanyImages(Integer id) {
        this.id = id;
    }

    public CompanyImages(Integer id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public CompanyInformation getCompanyId() {
        return companyId;
    }

    public void setCompanyId(CompanyInformation companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyImages)) {
            return false;
        }
        CompanyImages other = (CompanyImages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.CompanyImages[ id=" + id + " ]";
    }

    /**
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
}
