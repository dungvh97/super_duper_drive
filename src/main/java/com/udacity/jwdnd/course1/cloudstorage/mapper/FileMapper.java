package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND filename = #{fileName}")
    File getFile(Integer userId, String fileName);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    String[] getListFileName(Integer userId);
    
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    File[] getFiles(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}")
    void deleteFile(String fileName);
    
    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFileById(Integer fileId);
}
