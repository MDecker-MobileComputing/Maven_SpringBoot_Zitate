package de.eldecker.dhbw.spring.zitate.db;

import static jakarta.persistence.GenerationType.AUTO;

/*
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
*/

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Definition der Entität/DB-Tabelle für die Zitate.
 * Die eigentliche Zitate werden mit Lucene für die Volltextsuche
 * indiziert.
 */
@Entity
@Table( name = "Zitate" )
//@Indexed
public class ZitatEntity {

    /**
     * Primärschlüssel der Zitat-Entität.
     */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    //@FullTextField()
    @Column(name = "zitat", length = 9999)
    private String zitat;
}
