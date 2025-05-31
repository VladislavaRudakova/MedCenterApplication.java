package com.medCenter.medCenter.securityConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/";
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
            if (savedRequest.getRedirectUrl().equals("http://localhost:8080/client/clientTicket?continue")) {//!!!!!!!!!!!!!! for correction!
                redirectUrl = "/personal";
            }

        } else {
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if ("ROLE_ADMIN".equals(role)) {
                    redirectUrl = "/admin/start";
                } else if ("ROLE_CLIENT".equals(role)) {
                    redirectUrl = "/client/clientTickets";
                } else if ("ROLE_DOCTOR".equals(role)) {
                    redirectUrl = "/doctor/doctorStart";
                }
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
