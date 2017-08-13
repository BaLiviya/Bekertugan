package rbots.kz.SecurityService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rbots.kz.SecurityService.jpa.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
