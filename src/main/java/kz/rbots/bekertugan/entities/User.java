package kz.rbots.bekertugan.entities;


public final class User {
    private String role;
    private String firstName;
    private String lastName;
    private String title;
    private boolean male;
    private String email;
    private String location;
    private String phone;
    private Integer newsletterSubscription;
    private String website;
    private String bio;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Integer getNewsletterSubscription() {
        return newsletterSubscription;
    }

    public void setNewsletterSubscription(final Integer newsletterSubscription) {
        this.newsletterSubscription = newsletterSubscription;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(final boolean male) {
        this.male = male;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

}
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import javax.validation.constraints.NotNull;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//    public class User implements UserDetails{
//
//
//        private Long id;
//
//
//    private String username;
//
//
//    private String senderName;
//
//
//    private String email;
//
//
//    private String sec_usr_pwd;
//
//
//    private Date dt_cre;
//
//
//    private String usr_cre;
//
//
//    private Date dt_upd;
//
//
//    private String usr_upd;
//
//
//    private boolean status;
//
//
//    private Long dealerId;
//
//
//    private boolean firstLogin;
//
//
//    private List<Role> roles;
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username.trim();
//    }
//
//    @Override
//    public String getPassword() {
//        return sec_usr_pwd;
//    }
//
//    public void setPassword(String password) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String hashedPassowrd = passwordEncoder.encode(password.trim());
//        this.sec_usr_pwd = hashedPassowrd;
//    }
//
//    public boolean isStatus() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }
//
//    public List<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<Role> roles) {
//        this.roles = roles;
//    }
//
//    public String getSenderName() {
//        return senderName;
//    }
//
//    public void setSenderName(String senderName) {
//        this.senderName = senderName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public Date getDt_cre() {
//        return dt_cre;
//    }
//
//    public void setDt_cre(Date dt_cre) {
//        this.dt_cre = dt_cre;
//    }
//
//    public String getUsr_cre() {
//        return usr_cre;
//    }
//
//    public void setUsr_cre(String usr_cre) {
//        this.usr_cre = usr_cre;
//    }
//
//    public Date getDt_upd() {
//        return dt_upd;
//    }
//
//    public void setDt_upd(Date dt_upd) {
//        this.dt_upd = dt_upd;
//    }
//
//    public String getUsr_upd() {
//        return usr_upd;
//    }
//
//    public void setUsr_upd(String usr_upd) {
//        this.usr_upd = usr_upd;
//    }
//
//    public Long getDealerId() {
//        return dealerId;
//    }
//
//    public void setDealerId(Long dealerId) {
//        this.dealerId = dealerId;
//    }
//
//    public boolean isFirstLogin() {
//        return firstLogin;
//    }
//
//    public void setFirstLogin(boolean firstLogin) {
//        this.firstLogin = firstLogin;
//    }
//}
