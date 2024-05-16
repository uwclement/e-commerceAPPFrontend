package com.ecommerce;

//import com.ecommerce.dto.MultipartFileToByteArrayEditor;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

//	@Bean
//	public PropertyEditorRegistrar propertyEditorRegistrar() {
//		return new PropertyEditorRegistrar() {
//			@Override
//			public void registerCustomEditors(PropertyEditorRegistry registry) {
//				registry.registerCustomEditor(byte[].class, new MultipartFileToByteArrayEditor());
//			}
//		};
//	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}
}
