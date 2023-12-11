package edu.pav.PatientTrackerSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The UserLoginKey class represents the composite key for user login information.
 * It is an embeddable class used in entities that require user login details.
 * This key includes the user ID and username.
 */
@Data
@Embeddable
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class UserLoginKey implements Serializable {
    /**
     * The unique identifier for a user.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * The username associated with the user.
     */
    @Column(name = "user_name", nullable = false)
    private String userName;
}
