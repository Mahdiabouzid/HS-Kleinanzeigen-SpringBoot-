package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class AD {

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "ID", nullable = false)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "TYPE")
//  @Column()
  private Type type;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "CATEGORY_ID")
  @JsonManagedReference
  private Category category;

  @Column(nullable = false, name = "TITLE")
  private String title;

  @Column(nullable = false, name = "DESCRIPTION")
  private String description;

  @Column(name = "PRICE")
  private Integer price;

  @Column(name = "LOCATION")
  private String location;

  @CreatedDate
  @JsonIgnore
  @Column(nullable = false, name = "CREATED")
  private LocalDateTime created;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "USER_ID")
  @JsonManagedReference
  private User user;
}
