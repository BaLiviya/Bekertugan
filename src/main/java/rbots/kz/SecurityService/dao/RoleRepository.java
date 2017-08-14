package rbots.kz.SecurityService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbots.kz.SecurityService.jpa.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
