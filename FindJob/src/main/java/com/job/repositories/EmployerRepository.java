/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;
import com.job.pojo.Employer;
import java.util.List;

public interface EmployerRepository {
    List<Employer> getEmployers();
    Employer getEmployerById(int id);
}
