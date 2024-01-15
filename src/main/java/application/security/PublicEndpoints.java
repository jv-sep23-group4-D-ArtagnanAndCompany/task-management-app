package application.security;

import java.util.List;

public class PublicEndpoints {
    private static final List<String> publicEndpoints = List.of("/api/auth/login",
            "/api/auth/registration", "/api/health");

    public static List<String> getEndpoints() {
        return publicEndpoints;
    }
}
