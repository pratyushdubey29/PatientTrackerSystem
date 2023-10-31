package edu.pav.PatientTrackerSystem.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private Integer height;
    private Integer weight;
    private String DOB;
    private String name;
    private String sex;
}
