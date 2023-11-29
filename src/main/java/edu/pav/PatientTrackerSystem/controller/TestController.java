//package edu.pav.PatientTrackerSystem.controller;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//public class TestController {
//
//    @GetMapping("test-session")
//    public String getSession(HttpSession session){
//        if ( session.getAttribute("visit-counter") == null ){
//            session.setAttribute("visit-counter" , 1 );
//            log.info( "New user ");
//        } else {
//            log.info( "visit count : " + session.getAttribute("visit-counter")  );
//            session.setAttribute("visit-counter" , (int) session.getAttribute("visit-counter") + 1 );
//        }
//        return "test-session.html";
//    }
//
//}
