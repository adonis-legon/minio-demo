package com.example.miniodemo;

import com.example.miniodemo.storage.ObjectStoreManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	ObjectStoreManager objectStorageManager;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Using object store at: " + this.objectStorageManager.getServer());

		String objectName = java.util.UUID.randomUUID().toString();
		String objectContent = "12345";

		// create object from string
		this.objectStorageManager.putString(objectName, objectContent);

		// get object content as string
		String storedObjectContent = this.objectStorageManager.getString(objectName);
		
		// check content matches
		if(storedObjectContent.equals(objectContent)){
			System.out.println("Succeeded!");
		}
		else{
			System.out.println("Failed!");
		}

		// delete object
		if(args.length > 0 && args[0].equals("--delete")){
			this.objectStorageManager.delete(objectName);
		}

		System.out.println("Finished!");
	}

}
