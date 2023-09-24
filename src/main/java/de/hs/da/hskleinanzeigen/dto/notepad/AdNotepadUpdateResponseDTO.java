package de.hs.da.hskleinanzeigen.dto.notepad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdNotepadUpdateResponseDTO {
  private Integer id;
  private Integer advertisementId;
  private String note;
}

