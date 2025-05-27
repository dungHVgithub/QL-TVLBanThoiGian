package com.job.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.job.pojo.Employee;
import com.job.pojo.EmployeeJob;
import com.job.pojo.JobPosting;
import com.job.pojo.UserDocuments;
import com.job.services.EmployeeJobService;
import com.job.services.EmployeeService;
import com.job.services.JobPostingService;
import com.job.services.UserDocService;
import com.job.services.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserDocController {

    @Autowired
    private JobPostingService jobPostingService;
    @Autowired
    private UserDocService userDocService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeJobService employeeJobService; // Thêm EmployeeJobService
    @Autowired
    private Cloudinary cloudinary;

    @DeleteMapping("/user_documents/{userDocumentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "userDocumentId") int id) {
        this.userDocService.deleteUserDocs(id);
    }

    @GetMapping("/user_documents")
    public ResponseEntity<List<UserDocuments>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.userDocService.getUserDocs(params), HttpStatus.OK);
    }

    @GetMapping("/user_documents/{userDocumentId}")
    public ResponseEntity<UserDocuments> retrieve(@PathVariable(value = "userDocumentId") int id) {
        return new ResponseEntity<>(this.userDocService.getUserDocsById(id), HttpStatus.OK);
    }

    @PostMapping("/user_documents/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDocuments> create(@RequestBody UserDocuments userDocument) {
        UserDocuments savedDocument = this.userDocService.addOrUpdate(userDocument);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }

    @GetMapping("/user_documents/by_user/{userId}")
    public ResponseEntity<List<UserDocuments>> getDocumentsByUserId(@PathVariable("userId") int userId) {
        List<UserDocuments> docs = this.userDocService.getDocsByUserId(userId);
        return new ResponseEntity<>(docs, HttpStatus.OK);
    }

    @PutMapping("/user_documents/{id}")
    public ResponseEntity<UserDocuments> updateUserDocument(
            @PathVariable("id") int id,
            @RequestBody UserDocuments updatedDoc) {
        try {
            UserDocuments existing = this.userDocService.getUserDocsById(id);

            if (existing == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            existing.setDocumentType(updatedDoc.getDocumentType());
            existing.setDocumentPath(updatedDoc.getDocumentPath());
            UserDocuments saved = this.userDocService.addOrUpdate(existing);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user_documents/employee/{employeeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDocuments> addDocumentForEmployee(
            @PathVariable("employeeId") int employeeId,
            @RequestParam("documentType") String documentType,
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobId") int jobId) {
        try {
            // Tạo và lưu UserDocuments
            UserDocuments userDocument = new UserDocuments();
            userDocument.setDocumentType(documentType);
            userDocument.setName(name);
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String documentPath = uploadResult.get("url").toString();
            userDocument.setDocumentPath(documentPath);
            userDocument.setCreatedDate(new Date());
            UserDocuments savedDocument = this.userDocService.addOrUpdateForEmployee(userDocument, employeeId);

            // Tạo bản ghi EmployeeJob
            EmployeeJob employeeJob = new EmployeeJob();
            Employee employee = employeeService.getEmployeeById(employeeId);
            JobPosting jobPosting = jobPostingService.getJobById(jobId);
            if (employee == null || jobPosting == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            employeeJob.setEmployeeId(employee);
            employeeJob.setJobId(jobPosting);
            employeeJob.setJobState((short) 3); // Đặt jobState = 3
            employeeJobService.addOrUpdate(employeeJob);

            // Cập nhật jobPosting với employeeId (nếu cần, nhưng đoạn này có vẻ không cần thiết vì đã có EmployeeJob)
            if (jobPosting.getEmployeeId() == null) {
                jobPosting.setEmployeeId(employee);
                jobPostingService.addOrUpdate(jobPosting);
            }
            return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user_documents/employee/{employeeId}")
    public ResponseEntity<UserDocuments> updateDocumentForEmployee(
            @PathVariable("employeeId") int employeeId,
            @RequestBody UserDocuments updatedDoc) {
        try {
            List<UserDocuments> existingDocs = this.userDocService.getDocsByEmployeeId(employeeId);
            if (existingDocs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            UserDocuments existing = existingDocs.get(0);
            existing.setDocumentType(updatedDoc.getDocumentType());
            existing.setDocumentPath(updatedDoc.getDocumentPath());
            UserDocuments saved = this.userDocService.addOrUpdateForEmployee(existing, employeeId);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}