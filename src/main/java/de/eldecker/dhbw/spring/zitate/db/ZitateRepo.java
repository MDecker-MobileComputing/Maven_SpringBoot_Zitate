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
     * Funktioniert evtl. nicht, wenn andere Datenbank als "H2" verwendet wird.
     * <br><br>
     * 
     * Das SQL-Statement funktioniert unter H2 (und anscheinend auch MySQL),
     * aber nicht unter PostgreSQL: für PostgreSQL muss der Name der Funktion
     * "RAND" durch "RANDOM" ersetzt werden.
     * 
     * @return Optional mit zufällig ausgewähltem Zitat
     */
    @Query( nativeQuery = true, value = "SELECT * FROM zitate ORDER BY RAND() LIMIT 1" )
    Optional<ZitatEntity> getZufallsZitat();
}
