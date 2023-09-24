package de.hs.da.hskleinanzeigen.dto.user;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UserRequestDTO {
  @EqualsAndHashCode.Include
  private Integer id;
  private String email;
  private String firstName;
  private String lastName;
  private String phone;
  private String location;
}
