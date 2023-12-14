package edu.pav.PatientTrackerSystem;

import edu.pav.PatientTrackerSystem.commons.EmailServiceTest;
import edu.pav.PatientTrackerSystem.controller.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdminControllerTest.class,
//        DoctorControllerTest.class,
//        PatientControllerTest.class,
        CaseControllerTest.class,
        AppointmentControllerTest.class,
        EmailServiceTest.class
})
public class AllTestsSuite {
}