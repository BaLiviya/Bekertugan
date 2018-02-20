package kz.rbots.bekertugan.security;

public interface ISecurity {
    boolean tryToLogin(String username, String password);

}
