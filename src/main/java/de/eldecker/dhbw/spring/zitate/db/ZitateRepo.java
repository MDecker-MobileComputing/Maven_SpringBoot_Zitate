package de.eldecker.dhbw.spring.zitate.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Repository für Tabelle mit Zitaten.
 */
public interface ZitateRepo extends JpaRepository<ZitatEntity, Long> {

    /**
     * Native Query (!) für das Holen eines zufälligen Zitats, kein JPQL!
     * Funktioniert evtl. auf anderen Datenbank als H2 nicht.
     *
     * @return Optional mit zufällig ausgewähltem Zitat
     */
    @Query( nativeQuery = true, value = "SELECT * FROM zitate ORDER BY RAND() LIMIT 1" )
    Optional<ZitatEntity> getZufallsZitat();
}
