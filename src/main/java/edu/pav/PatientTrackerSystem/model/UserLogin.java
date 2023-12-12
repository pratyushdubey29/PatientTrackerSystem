package edu.pav.PatientTrackerSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

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
     * The composite key consisting of user_id and user_name.
     */
    @EmbeddedId
    private UserLoginKey loginKey;

    /**
     * The password associated with the user login.
     */
    @Column
    private String password;
}
