package com.mobicool.e.store.service.impl;

import com.mobicool.e.store.exception.BadApiRequest;
import com.mobicool.e.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        //acb.png
        String originalFilename = file.getOriginalFilename();
        logger.info("Filename :{}", originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".")); //if problem occur comment this and uncomment 27 line
       // String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;
        if (extension.equalsIgnoreCase(".png")  || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            File folder = new File(path);
            if (!folder.exists()) {
                //CREATE THE FOLDER
                folder.mkdirs();
            }
            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequest("File with this " + extension + "Not Allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path + File.separator + name;

        return  new FileInputStream(fullPath);
    }
}