package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper,  EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void addOrUpdateCredential(String userName, Credential credential) {
    	Integer credentialId = credential.getCredentialId();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
    	if (credentialId == null) {
    		addCredential(userName, credential.getUrl(), credential.getUserName(), encodedKey, encryptedPassword);
    	} else {
    		updateCredential(credentialId, credential.getUrl(), credential.getUserName(), encodedKey, encryptedPassword);
		}
    }
    
    public void addCredential(String userName, String url, String credentialUserName, String encodedKey, String encryptedPassword) {
        Integer userId = userMapper.getUser(userName).getUserId();
        
        Credential credential = new Credential(url, credentialUserName, encodedKey, encryptedPassword, userId);
        credentialMapper.insert(credential);
    }

    public Credential[] getCredentials(Integer userId) {
        return credentialMapper.getCredentials(userId);
    }

    public Credential getCredential(Integer noteId) {
        return credentialMapper.getCredential(noteId);
    }

    public void deleteCredential(Integer noteId) {
        credentialMapper.deleteCredential(noteId);
    }

    public void updateCredential(Integer credentialId, String url, String credentialUserName, String encodedKey, String encryptedPassword) {
        credentialMapper.updateCredential(new Credential(credentialId, url, credentialUserName, encodedKey, encryptedPassword, null));
    }
}
