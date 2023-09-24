package de.hs.da.hskleinanzeigen.service;

import de.hs.da.hskleinanzeigen.entity.Notepad;
import de.hs.da.hskleinanzeigen.repository.NotepadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotepadService {
  private final NotepadRepository notepadRepository;

  @Autowired
  public NotepadService(NotepadRepository notepadRepository) {
    this.notepadRepository = notepadRepository;
  }

  public Notepad save(Notepad notepad){
    return this.notepadRepository.save(notepad);
  }

  public List<Notepad> findAllByUserId(Integer userId){
    return this.notepadRepository.findAllByUserId(userId);
  }
  public Notepad findByUserId(Integer userId){
    return this.notepadRepository.findByUserId(userId).orElse(null);
  }

  public void deleteById(Integer id) {
    this.notepadRepository.deleteById(id);
  }

  public Notepad findByUserIdAndAdId(Integer user_id, Integer ad_id) {
    return this.notepadRepository.findByUserIdAndAdId(user_id, ad_id).orElse(null);

  }
}
