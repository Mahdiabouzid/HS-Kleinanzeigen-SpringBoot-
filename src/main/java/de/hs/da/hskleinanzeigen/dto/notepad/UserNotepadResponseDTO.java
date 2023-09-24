package de.hs.da.hskleinanzeigen.dto.notepad;

import lombok.Data;

@Data
public class UserNotepadResponseDTO {
  private Integer id;
  private AdNotepadResponseDTO advertisement;
  private String note;
}
