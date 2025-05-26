/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author AN515-57
 */
@Entity
@Table(name = "company_information")
@NamedQueries({
    @NamedQuery(name = "CompanyInformation.findAll", query = "SELECT c FROM CompanyInformation c"),
    @NamedQuery(name = "CompanyInformation.findById", query = "SELECT c FROM CompanyInformation c WHERE c.id = :id"),
    @NamedQuery(name = "CompanyInformation.findByName", query = "SELECT c FROM CompanyInformation c WHERE c.name = :name"),
    @NamedQuery(name = "CompanyInformation.findByAddress", query = "SELECT c FROM CompanyInformation c WHERE c.address = :address"),
    @NamedQuery(name = "CompanyInformation.findByTaxCode", query = "SELECT c FROM CompanyInformation c WHERE c.taxCode = :taxCode")})
public class CompanyInformation implements Serializable {

    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Size(max = 50)
    @Column(name = "tax_code")
    private String taxCode;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private Set<Employer> employerSet;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId")
    @JsonIgnore
    private Set<CompanyImages> companyImagesSet;

    public CompanyInformation() {
    }

    public CompanyInformation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Set<CompanyImages> getCompanyImagesSet() {
        return companyImagesSet;
    }

    public void setCompanyImagesSet(Set<CompanyImages> companyImagesSet) {
        this.companyImagesSet = companyImagesSet;
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
        if (!(object instanceof CompanyInformation)) {
            return false;
        }
        CompanyInformation other = (CompanyInformation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.CompanyInformation[ id=" + id + " ]";
    }


   

    public Set<Employer> getEmployerSet() {
        return employerSet;
    }

    public void setEmployerSet(Set<Employer> employerSet) {
        this.employerSet = employerSet;
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

 
    
}
