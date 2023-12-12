package edu.pav.PatientTrackerSystem.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * This abstract class represents the login information for a user.
 * It includes an embedded ID and password for authentication.
 */
@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class UserLogin {

    @Id
    @Column
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username associated with the user login.
     */
    @Column
    private String username;

    /**
     * The password associated with the user login.
     */
    @Column
    private String password;
}
