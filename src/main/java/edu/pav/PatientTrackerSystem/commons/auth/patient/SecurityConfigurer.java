//package edu.pav.PatientTrackerSystem.commons.auth.patient;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@EnableWebSecurity
//public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
//
//    private PasswordEncoder ourPasswordEncoder;
//    @Autowired
//    private MyUserDetailsService userDetailsService;
//
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(userDetailsService);
////    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        if(ourPasswordEncoder == null) {
//            ourPasswordEncoder = new BCryptPasswordEncoder();
//        }
//        return ourPasswordEncoder;
//    }
//
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}
