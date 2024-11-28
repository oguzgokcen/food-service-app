package com.user.dto;

import com.user.validation.UniqueEmail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Ad Soyad boş olamaz")
    private String fullName;

    @NotNull(message = "Email boş olamaz")
    @UniqueEmail
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Geçersiz email formatı")
    private String email;

    @NotNull(message = "Şifre boş olamaz")
    @Size(min = 5, max = 255, message = "Şifreniz en az 5, en fazla 255 karakter içermeli")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Şifreniz bir büyük bir küçük harf ve rakam içermeli")
    private String password;
}
