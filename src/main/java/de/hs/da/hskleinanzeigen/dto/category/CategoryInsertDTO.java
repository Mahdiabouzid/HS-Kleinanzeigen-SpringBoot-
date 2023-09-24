package de.hs.da.hskleinanzeigen.dto.category;

import lombok.Data;

@Data
public class CategoryInsertDTO {
  private Integer parentId;
  private String name;
}
