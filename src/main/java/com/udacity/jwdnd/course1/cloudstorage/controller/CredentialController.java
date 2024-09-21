package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping()
    public String newUpdateCredential(Authentication authentication, Credential credential, Model model) {
        String userName = authentication.getName();
        credentialService.addOrUpdateCredential(userName, credential);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping()
    public Credential[] getCredentials(Authentication authentication) {
    	String userName = authentication.getName();
    	userService.getUser(userName);
    	

        return credentialService.getCredentials(userService.getUser(userName).getUserId());
    }
 
    @GetMapping("/{credentialId}")
    public Credential getCredentialById(@PathVariable Integer credentialId) {
    	return credentialService.getCredential(credentialId);
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Authentication authentication, Model model) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("success", true);
        return "result";
    }
}
