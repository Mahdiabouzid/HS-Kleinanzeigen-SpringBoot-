package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "CATEGORY")
public class Category {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "ID")
  private Integer id;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
  @ToString.Exclude
  @JsonIgnore
 // @JsonBackReference
  private Set<AD> ADS;

  @ManyToOne
  @JoinColumn(name = "PARENT_ID")
  @JsonIgnore
  private Category parent;

  @Column(name = "NAME")
  private String name;
}
