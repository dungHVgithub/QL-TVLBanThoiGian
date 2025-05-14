/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.job.pojo.UserDocuments;
import com.job.repositories.UserDocRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DUNG
 */
@Repository
@Transactional
public class UserDocRepositoryImpl implements UserDocRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<UserDocuments> getUserDocs(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<UserDocuments> q = b.createQuery(UserDocuments.class);
        Root root = q.from(UserDocuments.class);
        q.select(root);

//        if (params != null) {
//            List<Predicate> predicates = new ArrayList<>();
//            String kw = params.get("kw");
//            if (kw != null && !kw.isEmpty()) {
//                predicates.add(b.like(root.get("description"), String.format("%%%s%%", kw)));
//            }
//
//            String fromPrice = params.get("fromSalary");
//            if (fromPrice != null && !fromPrice.isEmpty()) {
//                predicates.add(b.greaterThanOrEqualTo(root.get("salary"), fromPrice));
//            }
//
//            String toPrice = params.get("toSalary");
//            if (toPrice != null && !toPrice.isEmpty()) {
//                predicates.add(b.lessThanOrEqualTo(root.get("salary"), toPrice));
//            }
//
//            String cateId = params.get("categoryId");
//            if (cateId != null && !cateId.isEmpty()) {
//                predicates.add(b.equal(root.get("categoryId").as(Integer.class), cateId));
//            }
//
//            q.where(predicates.toArray(Predicate[]::new));
//            String orderBy = params.get("orderBy");
//            if (orderBy != null && !orderBy.isEmpty()) {
//                q.orderBy(b.asc(root.get(orderBy)));
//            }
//        }
        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public UserDocuments getUserDocsById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(UserDocuments.class, id);
    }

    @Override
    public UserDocuments addOrUpdate(UserDocuments j) {
        Session s = this.factory.getObject().getCurrentSession();
        if (j.getId() == null) {
            s.persist(j);
        } else {
            s.merge(j);
        }

        return j;
    }

    @Override
    public void deleteUserDocs(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        UserDocuments j = this.getUserDocsById(id);
        s.remove(j);
    }

    public UserDocRepositoryImpl() {
        String path = new File("src/main/resources/cccd-gcv.json").getAbsolutePath();
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", path);
    }

    @Override
    public String extractTextFromImage(byte[] fileBytes) throws Exception {
        ByteString imgBytes = ByteString.copyFrom(fileBytes);
        Image image = Image.newBuilder().setContent(imgBytes).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(request));
            AnnotateImageResponse imageResponse = response.getResponses(0);

            if (imageResponse.hasError()) {
                throw new Exception("Google Vision API Error: " + imageResponse.getError().getMessage());
            }

            return imageResponse.getFullTextAnnotation().getText();
        }
    }

    

}
