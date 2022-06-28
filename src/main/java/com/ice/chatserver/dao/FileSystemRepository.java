package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {
    void deleteByFilePath(String filePath);
}