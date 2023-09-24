package de.hs.da.hskleinanzeigen.repository;

import de.hs.da.hskleinanzeigen.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  @Override
  Optional<Category> findById(Integer integer);

  Optional<Category> findByName(String name);
}
