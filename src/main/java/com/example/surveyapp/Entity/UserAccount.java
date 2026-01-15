package com.example.surveyapp.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users") // Genelde 'users' tablosu tercih edilir
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Diyagramda yoktu ama güvenlik için gereklidir

    private String role; // Örn: ROLE_USER, ROLE_ADMIN

    @Transient

    private String token; // DB'ye kaydedilmez, sadece login sonrası döner

    // --- Spring Security (UserDetails) Metotları ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Rolleri Spring'in anlayacağı 'GrantedAuthority' listesine çeviririz
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email; // Giriş yaparken username olarak e-posta kullanıyoruz
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
    @Override
    public String getPassword() {
        return this.password; // Entity içindeki 'password' alanını döner
    }
}