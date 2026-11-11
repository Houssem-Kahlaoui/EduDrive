package tn.enicarthage.eduDrive.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {}

    public static String hash(String plain) {
        if (plain == null) return null;
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }


    public static boolean verify(String plain, String hashed) {
        if (plain == null || hashed == null) return false;
        if (hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(plain, hashed);
            } catch (IllegalArgumentException e) {
                return false; 
            }
        }
        return plain.equals(hashed);
    }
}