package tn.enicarthage.eduDrive.exception;

import tn.enicarthage.eduDrive.dto.ErrorResponse;

public class GlobalExceptionHandler {

    public static ErrorResponse handle(Exception e) {
        if (e instanceof ResourceNotFoundException) {
            return new ErrorResponse(404, e.getMessage());
        }
        if (e instanceof AuthenticationException) {
            return new ErrorResponse(401, e.getMessage());
        }
        if (e instanceof ValidationException) {
            return new ErrorResponse(400, e.getMessage());
        }
        if (e instanceof ConflictException) { 
            return new ErrorResponse(409, e.getMessage());
        }
        
        e.printStackTrace(); 
        return new ErrorResponse(500, "Erreur interne du serveur : " + e.getMessage());
    }
}