/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AN515-57
 */
@Entity
@Table(name = "user_documents")
@NamedQueries({
    @NamedQuery(name = "UserDocuments.findAll", query = "SELECT u FROM UserDocuments u"),
    @NamedQuery(name = "UserDocuments.findById", query = "SELECT u FROM UserDocuments u WHERE u.id = :id"),
    @NamedQuery(name = "UserDocuments.findByDocumentType", query = "SELECT u FROM UserDocuments u WHERE u.documentType = :documentType"),
    @NamedQuery(name = "UserDocuments.findByDocumentPath", query = "SELECT u FROM UserDocuments u WHERE u.documentPath = :documentPath"),
    @NamedQuery(name = "UserDocuments.findByCreatedDate", query = "SELECT u FROM UserDocuments u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "UserDocuments.findByStatus", query = "SELECT u FROM UserDocuments u WHERE u.status = :status")})
public class UserDocuments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 7)
    @Column(name = "document_type")
    private String documentType;
    @Size(max = 255)
    @Column(name = "document_path")
    private String documentPath;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Size(max = 8)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "approved_by_admin_id", referencedColumnName = "id")
    @ManyToOne
    private Admin approvedByAdminId;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne
    private User userid;

    public UserDocuments() {
    }

    public UserDocuments(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Admin getApprovedByAdminId() {
        return approvedByAdminId;
    }

    public void setApprovedByAdminId(Admin approvedByAdminId) {
        this.approvedByAdminId = approvedByAdminId;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
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
        if (!(object instanceof UserDocuments)) {
            return false;
        }
        UserDocuments other = (UserDocuments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.UserDocuments[ id=" + id + " ]";
    }
    
}
