package de.hs.da.hskleinanzeigen.dto.notepad;

import de.hs.da.hskleinanzeigen.dto.category.CategoryAdDTO;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import de.hs.da.hskleinanzeigen.dto.user.UserRequestDTO;
import lombok.Data;

@Data
public class AdNotepadResponseDTO {
  private Integer id;
  private Type type;
  private CategoryAdDTO category;
  private UserRequestDTO user;
  private String title;
  private String description;
  private Integer price;
  private String location;
}
