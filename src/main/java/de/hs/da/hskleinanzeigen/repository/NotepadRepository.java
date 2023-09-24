package de.hs.da.hskleinanzeigen.repository;

import de.hs.da.hskleinanzeigen.entity.Notepad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotepadRepository extends JpaRepository<Notepad, Integer> {
  List<Notepad> findAllByUserId(Integer userId);
  Optional<Notepad> findById(Integer id);
  Optional<Notepad> findByUserId(Integer userId);

  void deleteById(Integer id);

  Optional<Notepad> findByUserIdAndAdId(Integer user_id, Integer ad_id);
}
