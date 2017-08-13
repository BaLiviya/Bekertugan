package rbots.kz.SecurityService.service;

import rbots.kz.SecurityService.jpa.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);
}
