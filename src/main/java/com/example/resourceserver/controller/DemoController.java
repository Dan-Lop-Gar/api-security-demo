package com.example.resourceserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/public/hello")
    public String publicEndpoint() {
        return "Este endpoint es público";
    }

    @GetMapping("/public/test")
    public String publicTestEndpoint() {
        return "Este endpoint es público y para tests";
    }

    @GetMapping("/public/upgrade")
    public String publicUpgradeEndpoint() {
        return "Este endpoint es público y para upgrade";
    }

    @GetMapping("/public/apiVersion")
    public ResponseEntity<String> publicAPIVersion() {
        return ResponseEntity.ok("Latest version");
    }

    //@PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/api/hello")
    public String protectedEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return "Hola " + jwt.getSubject() + " | scopes: " + jwt.getClaimAsStringList("scope");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/hello")
    public String adminEndpoint() {
        return "Solo accesible con scope write";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    @GetMapping("/api/roles")
    public Object roles(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaim("roles");
    }

    @GetMapping("/api/users/{username}")
    @PostAuthorize("returnObject.body.get('name') == authentication.name")
    public ResponseEntity<Map<String, String>> getUserProfile(@PathVariable String username){
        Map<String,String> userDetails = new HashMap<>();
        userDetails.put("name",username);

        return ResponseEntity.ok(userDetails);
    }

    // Ejecuta el método pero solo devuelve el resultado
// si el subject del token coincide con el username del objeto retornado
//    @GetMapping("/api/users/{username}")
//    @PostAuthorize("returnObject.username == authentication.name")
//    public UserDto getUserProfile(@PathVariable String username) {
//        // Este código SÍ se ejecuta siempre
//        // pero el resultado solo se devuelve si pasa el @PostAuthorize
//        return userService.findByUsername(username);
//    }
}
