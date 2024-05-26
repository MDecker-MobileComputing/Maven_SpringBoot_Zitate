package de.eldecker.dhbw.spring.zitate.db;

import static jakarta.persistence.GenerationType.AUTO;
import static java.lang.String.format;

import java.util.Objects;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Definition der Entität/DB-Tabelle für die Zitate.
 * Die eigentliche Zitate werden mit Lucene für die Volltextsuche
 * indiziert.
 * <br><br>
 * 
 * Lucene wird seinen Index für diese Tabelle im Unterordner
 * {@code ZitatEntity/} im Wurzelverzeichnis des Maven-Projekts
 * ablegen.
 */
@Entity
@Table( name = "Zitate" )
@Indexed
public class ZitatEntity {

    /**
     * Primärschlüssel der Zitat-Entität.
     */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

	/**
	 * Feld mit eigentlichem Zitat, wird von Lucene indiziert.
	 */
    @FullTextField()
    @Column(name = "zitat", length = 9999)
    private String zitat;


    /**
     * Default-Konstruktor, wird von JPA benötigt.
     */
    public ZitatEntity() {

    	this.zitat = "";
    }
    
    
    /**
     * Konstruktor um bei Objekterzeugung gleich den Text des Zitats
     * zu übergeben.
     * 
     * @param zitat Text des Zitats
     */
    public ZitatEntity( String zitat ) {
    	
    	this.zitat = zitat;
    }


    /**
     * Getter für ID des Zitats.
     * Es gibt keinen zugehörigen Setter, weil die ID von JPA
     * verwaltet wird.
     *
     * @return ID/Primärschlüssel
     */
	public Long getId() {

		return id;
	}


	/**
	 * Getter für eigentlichen Text des Zitats.
	 *
	 * @return Zitat
	 */
	public String getZitat() {

		return zitat;
	}


	/**
	 * Setter für den eigentlichen Text des Zitats
	 *
	 * @param zitat Text des Zitats
	 */
	public void setZitat( String zitat ) {

		this.zitat = zitat;
	}


	/**
	 * String-Repräsentation des Objekts.
	 *
	 * @return String mit ID und Text des Zitats.
	 */
	@Override
	public String toString() {

		final String str = format( "Zitat Nr %d: %s", id, zitat );

		return str;
	}


	/**
	 * Methode berechnet Hash-Wert, der eindeutig für das aufrufende Objekt
	 * sein sollte. Die ID geht nicht in die Hash-Berechnung ein, weil diese
	 * evtl. von JPA noch nicht gesetzt ist.
	 *
	 * @return Hash-Wert
	 */
	@Override
	public int hashCode() {

		return Objects.hash( zitat );
	}


	/**
	 * Vergleich des aufrufenden Objekts mit anderem Objekt {@code obj}.
	 *
	 * @param obj Zu vergleichendes Objekt
	 *
	 * @return {@code true} gdw. {obj} auch eine Instanz von {@link ZitatEntity}
	 *         ist und die relevanten Attribute (aber nicht die ID, die ist evtl.
	 *         noch nicht von JPA gesetzt) denselben Wert haben.
	 */
    @Override
    public boolean equals( Object obj ) {

        if ( this == obj ) {

            return true;
        }
        if ( obj == null ) {

            return false;
        }

        if ( obj instanceof ZitatEntity anderesObjekt ) {

        	return zitat.equals( anderesObjekt.zitat );

        } else {

        	return false;
        }
	}

}
