package de.hs.da.hskleinanzeigen.dto.user;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String password;
}
