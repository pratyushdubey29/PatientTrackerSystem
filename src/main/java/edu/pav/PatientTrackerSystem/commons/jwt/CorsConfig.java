//package edu.pav.PatientTrackerSystem.commons.jwt;
//
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        String[] allowDomains = new String[2];
//        allowDomains[0] = "http://localhost:4200";
//        allowDomains[1] = "http://localhost:8080";
//
//        registry.addMapping("/**").allowedOrigins(allowDomains);
//    }
//}
