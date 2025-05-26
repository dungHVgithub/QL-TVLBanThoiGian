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
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author User
 */
@Entity
@Table(name = "user_notification")
@NamedQueries({
    @NamedQuery(name = "UserNotification.findAll", query = "SELECT u FROM UserNotification u"),
    @NamedQuery(name = "UserNotification.findById", query = "SELECT u FROM UserNotification u WHERE u.id = :id"),
    @NamedQuery(name = "UserNotification.findByIsRead", query = "SELECT u FROM UserNotification u WHERE u.isRead = :isRead"),
    @NamedQuery(name = "UserNotification.findByReadTime", query = "SELECT u FROM UserNotification u WHERE u.readTime = :readTime")})
public class UserNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(name = "read_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTime;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    @ManyToOne
    private Notification notificationId;

    public UserNotification() {
    }

    public UserNotification(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Notification getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Notification notificationId) {
        this.notificationId = notificationId;
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
        if (!(object instanceof UserNotification)) {
            return false;
        }
        UserNotification other = (UserNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.UserNotification[ id=" + id + " ]";
    }
    
}
