package rbots.kz.SecurityService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import rbots.kz.SecurityService.dao.UserRepository;
import rbots.kz.SecurityService.exception.UserAlreadyExistException;
import rbots.kz.SecurityService.jpa.User;
import rbots.kz.SecurityService.security.Role;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User add(User user) throws UserAlreadyExistException {
        Assert.notNull(user, "User object cannot be null");
        Assert.isNull(user.getId(), "User id filed must be null");
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("User already exist with username: " + user.getUsername());
        }
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.ROLE_USER);
        logger.info("Saving user: {}", user);
        return userRepository.save(user);
    }
}
