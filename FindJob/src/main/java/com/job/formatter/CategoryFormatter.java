/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.formatter;

import java.text.ParseException;
import java.util.Locale;

import com.job.pojo.Category;
import org.springframework.format.Formatter;

/**
 *
 * @author AN515-57
 */
public class CategoryFormatter implements Formatter<Category> {

    @Override
    public String print(Category category, Locale locale) {
        return String.valueOf(category.getId());
    }

    @Override
    public Category parse(String cateId, Locale locale) throws ParseException {
        Category c = new Category();
        c.setId(Integer.valueOf(cateId));
        return c;
    }
    
}
