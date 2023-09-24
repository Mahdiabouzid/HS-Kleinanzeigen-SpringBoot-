package de.hs.da.hskleinanzeigen.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = "NOTEPAD")
public class Notepad {
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "AD_ID")
  private AD ad;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "CREATED", nullable = false)
  private LocalDateTime created;


}
