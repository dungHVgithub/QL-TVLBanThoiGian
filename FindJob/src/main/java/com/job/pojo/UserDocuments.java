/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DUNG
 */
@Entity
@Table(name = "user_documents")
@NamedQueries({
    @NamedQuery(name = "UserDocuments.findAll", query = "SELECT u FROM UserDocuments u"),
    @NamedQuery(name = "UserDocuments.findById", query = "SELECT u FROM UserDocuments u WHERE u.id = :id"),
    @NamedQuery(name = "UserDocuments.findByDocumentType", query = "SELECT u FROM UserDocuments u WHERE u.documentType = :documentType"),
    @NamedQuery(name = "UserDocuments.findByDocumentPath", query = "SELECT u FROM UserDocuments u WHERE u.documentPath = :documentPath"),
    @NamedQuery(name = "UserDocuments.findByCreatedDate", query = "SELECT u FROM UserDocuments u WHERE u.createdDate = :createdDate")})
public class UserDocuments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne  
    private Employee employeeId;

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

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
