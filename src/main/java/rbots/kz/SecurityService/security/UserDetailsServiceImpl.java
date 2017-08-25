package rbots.kz.SecurityService.security;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rbots.kz.SecurityService.dao.UserRepository;
import rbots.kz.SecurityService.jpa.User;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl( UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) throw new UsernameNotFoundException("Cannot find user with username: " + username);
        log.info("load user {}", user.get().getUsername());
        return new UserDetailsImpl(user.get());
    }
}
