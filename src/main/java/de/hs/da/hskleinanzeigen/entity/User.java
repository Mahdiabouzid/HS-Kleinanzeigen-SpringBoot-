package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "USER")
public class User implements Serializable{
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "ID", nullable = false)
  private Integer id;

  @Column(name = "EMAIL", nullable = false)
  @NotNull(message = "Email cannot be null")
  @Email(message = "Email should be valid")
  private String email;

  @JsonIgnore
  @Column(name = "PASSWORD", nullable = false)
  @NotNull(message = "Password cannot be null")
  @Size(min = 6, message = "password should be at least 6 characters long")
  private String password;

  @Column(name = "FIRST_NAME")
  @Size(max = 255, message = "First name should not be greater than 255 characters")
  private String firstName;

  @Column(name = "LAST_NAME")
  @Size(max = 255, message = "Last name should not be greater than 255 characters")
  private String lastName;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "LOCATION")
  private String location;

  @JsonIgnore
  @CreatedDate
  @Column(name = "CREATED", nullable = false)
  private LocalDateTime created;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
  @ToString.Exclude
  @JsonIgnore
  // @JsonBackReference
  private Set<AD> ADS = new java.util.LinkedHashSet<>();

//  @OneToOne
//  @JoinColumn(name = "notepad_id")
//  private Notepad notepad;


}
