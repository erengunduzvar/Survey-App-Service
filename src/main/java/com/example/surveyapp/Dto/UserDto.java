package com.example.surveyapp.Dto;

import com.example.surveyapp.Entity.UserAccount;

/**
 * Kullanıcı bilgilerini güvenli bir şekilde dışarı açmak için kullanılan Record.
 * Şifre (password) alanını asla içermez.
 */
public record UserDto(
        Long userId,      // OpenAPI: userId
        String email,     // OpenAPI: email
        String role,      // OpenAPI: role
        Long id,          // OpenAPI: id
        String token      // OpenAPI: token
) {
    public static UserDto mapToDto(UserAccount entity) {
        return new UserDto(
                entity.getId(),    // userId
                entity.getEmail(), // email
                entity.getRole(),  // role
                entity.getId(),    // id
                entity.getToken()  // token (Login sonrası set edilmiş hali)
        );
    }
}
