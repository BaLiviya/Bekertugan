package rbots.kz.SecurityService.jpa;

import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import rbots.kz.SecurityService.security.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotEmpty
    @Size(min = 8, max = 32)
    @Column(name = "username")
    private String username;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Size(max = 100)
    private String firstName;

    @NotEmpty
    @Size(max = 100)
    private String lastName;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    private Role role;

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
