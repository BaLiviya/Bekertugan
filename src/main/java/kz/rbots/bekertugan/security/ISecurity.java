package kz.rbots.bekertugan.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface ISecurity {
    UserDetails tryToLoginAndGetUser(String username, String password);


}
