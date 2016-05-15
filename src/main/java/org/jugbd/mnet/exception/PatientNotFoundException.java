package org.jugbd.mnet.exception;

/**
 * @author Bazlur Rahman Rokon
 * @since 5/15/16.
 */
public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("Patient not found by id - " + id);
    }


    public PatientNotFoundException(String message) {
        super(message);
    }
}
