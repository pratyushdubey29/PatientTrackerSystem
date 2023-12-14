package edu.pav.PatientTrackerSystem.commons.jwt;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.model.UserLogin;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private DoctorSignupRepository doctorSignupRepository;

    @Autowired
    private PatientSignupRepository patientSignupRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String appendedUsername) throws UsernameNotFoundException {
        UserLogin user = null;
        String username;

        if (appendedUsername.startsWith(Constants.DOCTOR + Constants.COLON)) {
             username = appendedUsername.substring(7);
             user = doctorSignupRepository.findByUsername(username);
        } else {
            username = appendedUsername.substring(8);
            user = patientSignupRepository.findByUsername(username);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }
}