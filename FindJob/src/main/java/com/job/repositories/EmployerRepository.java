/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;
import java.util.List;
import com.job.pojo.Employer;

public interface EmployerRepository {
    List<Employer> getEmployers();
    Employer getEmployerById(int id);
}
