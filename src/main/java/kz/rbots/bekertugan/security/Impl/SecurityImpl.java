package kz.rbots.bekertugan.security.Impl;

import kz.rbots.bekertugan.security.ISecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityImpl implements ISecurity {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier(value="CustomUserDetailService")
    private UserDetailsService userDetailsService;



    @Override
    public boolean tryToLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null && new BCryptPasswordEncoder().matches(password,userDetails.getPassword())){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            //залогинились
            }
            return true;
        } else {
         return false;
        }
    }

}
