package rbots.kz.SecurityService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rbots.kz.SecurityService.jpa.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
