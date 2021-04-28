package com.main.glory;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.main.glory.schedulers.SingletonManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;


@ComponentScan({"com.main.glory.controller","com.main.glory.servicesImpl","com.main.glory.config","com.main.glory.filters","com.main.glory.utils","com.main.glory.*"})
@EnableJpaRepositories({"com.main.glory.Dao"})
@SpringBootApplication
public class GloryautofabApplication {
	public static void main(String[] args) {

		/*// Set Cloudinary instance
		//backup on the cloud
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "dvvqdgl3s",
				"api_key", "841845242494588",
				"api_secret", "E1owKNvkJZa131NBcDYEM6mdZSc"));
		SingletonManager manager = new SingletonManager();
		manager.setCloudinary(cloudinary);
		manager.init();

*/
		SpringApplication.run(GloryautofabApplication.class, args);
	}
}
