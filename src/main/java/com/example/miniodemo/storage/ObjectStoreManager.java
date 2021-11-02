package com.example.miniodemo.storage;

public interface ObjectStoreManager{
    String getServer();

    void putString(String name, String content) throws Exception;

    void putBytes(String name, byte[] content) throws Exception;

    String getString(String name) throws Exception;

    byte[] getBytes(String name) throws Exception;

    void delete(String name) throws Exception;
}