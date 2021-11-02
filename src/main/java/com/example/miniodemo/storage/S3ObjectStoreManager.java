package com.example.miniodemo.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Component
public class S3ObjectStoreManager implements ObjectStoreManager{
    @Value("${storage.server}")
    private String server;

    @Value("${storage.access-key}")
    private String accessKey;

    @Value("${storage.secret-key}")
    private String secretKey;

    @Value("${storage.bucket}")
    private String bucket;

    private MinioClient minioClient;

    @PostConstruct
    public void init() throws InvalidEndpointException, InvalidPortException {
        this.minioClient = new MinioClient(this.server, this.accessKey, this.secretKey);
    }

    public void put(String name, String content) throws Exception {

    }

    public String get(String name) throws Exception {
        try{
            InputStream contentStream = this.minioClient.getObject(this.bucket, name);

            byte[] contentByteArray = new byte[contentStream.available()];
            contentStream.read(contentByteArray);

            return new String(contentByteArray);
        }
        catch(Exception ex){
            throw new Exception("Error getting object. Message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void putString(String name, String content) throws Exception {
        this.putBytes(name, content.getBytes("UTF-8"));
    }

    @Override
    public void putBytes(String name, byte[] content) throws Exception {
        try{
            this.minioClient.putObject(this.bucket, name, new ByteArrayInputStream(content), "application/octet-stream");
        }
        catch(Exception ex){
            throw new Exception("Error putting object. Message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getString(String name) throws Exception {
        return new String(this.getBytes(name));
    }

    @Override
    public byte[] getBytes(String name) throws Exception {
        try{
            ObjectStat objectStat = this.minioClient.statObject(this.bucket, name);
            InputStream contentStream = this.minioClient.getObject(this.bucket, name);

            byte[] contentByteArray = new byte[(int)objectStat.length()];
            while ((contentStream.read(contentByteArray, 0, contentByteArray.length)) >= 0);
            contentStream.close();

            return contentByteArray;
        }
        catch(Exception ex){
            throw new Exception("Error getting object. Message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(String name) throws Exception {
        try{
            this.minioClient.removeObject(this.bucket, name);
        }
        catch(Exception ex){
            throw new Exception("Error deleting object. Message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getServer() {
        return this.server;
    }
}