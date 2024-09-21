package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {
    private final FileMapper fileMapper;
    
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public String[] getListFileName(Integer userId) {
        return fileMapper.getListFileName(userId);
    }

    public File[] getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }

    public void addFile(Integer userId, MultipartFile multipartFile) throws IOException {
        InputStream fis = multipartFile.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = fis.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] fileData = buffer.toByteArray();

        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());
        File file = new File(0, fileName, contentType, fileSize, userId, fileData);
        fileMapper.insert(file);
    }

//    public File getFile(String fileName) {
//        return fileMapper.getFile(fileName);
//    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(String fileName) {
        fileMapper.deleteFile(fileName);
    }

    public void deleteFileById(Integer fileId) {
        fileMapper.deleteFileById(fileId);
    }
    
    public boolean isFileExist(Integer userId, String fileName) {
        return fileMapper.getFile(userId, fileName) != null;
    }
}
