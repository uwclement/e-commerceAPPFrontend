//package com.ecommerce;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        exposeDirectory("images/products/",registry);
//    }
//
//    private void exposeDirectory(String DirName, ResourceHandlerRegistry registry) {
//        Path UploadDir = Paths.get(DirName);
//        String UploadPath = UploadDir.toFile().getAbsolutePath();
//        registry.addResourceHandler("/"+DirName+"/**").addResourceLocations("file:"+UploadPath);
//    }
//}
