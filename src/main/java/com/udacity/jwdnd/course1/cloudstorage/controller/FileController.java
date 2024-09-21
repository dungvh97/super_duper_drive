package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("/files")
public class FileController {

    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }


    @PostMapping
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication,  Model model) throws IOException {
    	System.out.println("fileUpload");

        String loggedInUserName = authentication.getName();
        User user = userService.getUser(loggedInUserName);

        if (fileUpload.getOriginalFilename() == null ||fileUpload.getOriginalFilename().length() == 0) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Please add valid file.");
            return "result";
        }

        if (fileService.isFileExist(user.getUserId(), fileUpload.getOriginalFilename())) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "The file already exists.");
            return "result";
        }

        fileService.addFile(user.getUserId(), fileUpload);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication authentication, Model model){
        fileService.deleteFileById(fileId);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFileName()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
