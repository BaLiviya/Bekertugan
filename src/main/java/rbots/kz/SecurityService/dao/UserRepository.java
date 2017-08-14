package rbots.kz.SecurityService.dao;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbots.kz.SecurityService.jpa.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(@NonNull String username);
}
