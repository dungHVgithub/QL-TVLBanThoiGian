/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DUNG
 */
public class CCCDParser {
    public static Map<String, String> parse(String text) {
        Map<String, String> result = new HashMap<>();

        Pattern namePattern = Pattern.compile("Họ tên[:\\s]+([\\p{L}\\s]+)");
        Matcher mName = namePattern.matcher(text);
        if (mName.find()) result.put("name", mName.group(1).trim());

        Pattern dobPattern = Pattern.compile("Ngày sinh[:\\s]+(\\d{2}/\\d{2}/\\d{4})");
        Matcher mDob = dobPattern.matcher(text);
        if (mDob.find()) result.put("birthday", mDob.group(1).trim());

        Pattern addressPattern = Pattern.compile("Địa chỉ[:\\s]+(.+)");
        Matcher mAddr = addressPattern.matcher(text);
        if (mAddr.find()) result.put("address", mAddr.group(1).trim());

        return result;
    }
}
