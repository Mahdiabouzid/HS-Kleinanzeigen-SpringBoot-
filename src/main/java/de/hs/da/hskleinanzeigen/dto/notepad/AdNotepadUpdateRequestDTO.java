package de.hs.da.hskleinanzeigen.dto.notepad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdNotepadUpdateRequestDTO {
  private Integer advertisementId;
  private String note;
}
