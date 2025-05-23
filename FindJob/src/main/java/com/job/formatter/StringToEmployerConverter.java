package com.job.formatter;
import com.job.pojo.Employer;
import com.job.services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEmployerConverter implements Converter<String, Employer> {
    @Autowired
    private EmployerService employerService;

    @Override
    public Employer convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            Integer employerId = Integer.parseInt(source);
            return employerService.getEmployerById(employerId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
