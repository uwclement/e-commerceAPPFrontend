//package com.ecommerce.service;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//public class FileUploadUtil {
//    public static void saveFile(String uploadDir,String fileName, MultipartFile multipartFile)throws Exception
//    {
//        Path uploadPath = Paths.get(uploadDir);
//
//        if (!Files.exists(uploadPath)){
//            Files.createDirectory(uploadPath);
//        }
//        try (InputStream inputStream = multipartFile.getInputStream();)
//        {
//         Path FilePath = uploadPath.resolve(fileName);
//         Files.copy(inputStream, FilePath, StandardCopyOption.REPLACE_EXISTING);
//        }catch (IOException e)
//        {throw  new IOException("could not save file"+ fileName,e);
//
//        }
//    }
//}
