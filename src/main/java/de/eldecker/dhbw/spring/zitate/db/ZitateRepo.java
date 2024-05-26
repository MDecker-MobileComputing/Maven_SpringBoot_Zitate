package de.eldecker.dhbw.spring.zitate.db;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository für Tabelle mit Zitaten.
 */
public interface ZitateRepo extends JpaRepository<ZitatEntity, Long> {

}
