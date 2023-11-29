//package edu.pav.PatientTrackerSystem.commons.auth.patient;
//
//import edu.pav.PatientTrackerSystem.model.PatientsLogin;
//import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private PatientSignupRepository signupRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<PatientsLogin> patientsLogin = signupRepository.findById(username);
//        return patientsLogin.map(login -> new User(login.getUserName(), login.getPassword(), new ArrayList<>())).orElse(null);
//    }
//}
