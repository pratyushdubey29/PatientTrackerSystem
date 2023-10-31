package edu.pav.PatientTrackerSystem.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientLogin {
    private String PatientId;
    private String Password;
}
