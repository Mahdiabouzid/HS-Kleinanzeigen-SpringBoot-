package de.hs.da.hskleinanzeigen.dto.Ad;

import de.hs.da.hskleinanzeigen.dto.category.CategoryRequestDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserAdRequestDTO;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import lombok.Data;

@Data
public class ADDto {
  private Type type;
  private CategoryRequestDTO category;
  private String title;
  private String description;
  private Integer price;
  private String location;
  private UserAdRequestDTO user;
}
