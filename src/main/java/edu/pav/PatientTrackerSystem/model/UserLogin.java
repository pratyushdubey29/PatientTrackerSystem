package edu.pav.PatientTrackerSystem.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public abstract class UserLogin {

    /**
     * The composite key consisting of user ID and user type.
     */
    @EmbeddedId
    private UserLoginKey loginKey;

    /**
     * The password associated with the user login.
     */
    @Column
    private String password;
}
