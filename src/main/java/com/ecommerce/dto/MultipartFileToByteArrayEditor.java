//package com.ecommerce.dto;
//
//import java.beans.PropertyEditorSupport;
//import java.io.IOException;
//
//import org.springframework.web.multipart.MultipartFile;
//
//public class MultipartFileToByteArrayEditor extends PropertyEditorSupport {
//    @Override
//    public void setValue(Object value) {
//        if (value == null) {
//            super.setValue(null);
//        } else {
//            try {
//                MultipartFile multipartFile = (MultipartFile) value;
//                super.setValue(multipartFile.getBytes());
//            } catch (IOException e) {
//                throw new IllegalArgumentException("Failed to read MultipartFile contents", e);
//            }
//        }
//    }
//}
