package de.eldecker.dhbw.spring.zitate.db;

import jakarta.persistence.EntityManager;

import java.util.List;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Diese Repo-Bean implementiert die Fuzzy-Suche mit Lucene. 
 */
@Repository
public class FuzzySuche {

	/** Zentrales API-Objekt für JPA (welches wir bei "Spring Data JPA" normalerweise nicht brauchen). */
	private final EntityManager _entityManager;
	
	
    /**
     * Konstruktor für <i>Dependency Injection</i>. 
     */
	@Autowired
	public FuzzySuche( EntityManager entityManager ) {
	    
	    _entityManager = entityManager;
	}
	

	/**
	 * Methode für Fuzzy-Suche nach Zitaten.
	 * <br><br>
	 * 
	 * Vorlage für diese Methode:
	 * <a href="https://www.springcloud.io/post/2022-04/spring-boot-hibernate-search/">dieser Artikel</a> 
	 * 
	 * @param suchbegriff Suchbegriff
	 * 
	 * @param maxTreffer Max. Treffer, die zurück geliefert werden soll
	 * 
	 * @param maxEditDistance Wert für max. erlaubte "Unschärfe" bei Fuzzy-Search;
	 *                        je höher der Wetter, desto "ungenauere" Treffer werden
	 *                        noch gefunden
	 *                        
	 * @return Liste der Ergebnisse
	 */
	public List<ZitatEntity> searchFuzzy( String suchbegriff, int maxTreffer, int maxEditDistance ) {
		
        final SearchSession searchSession = Search.session( _entityManager );

        return searchSession.search( ZitatEntity.class )
                            .where( f -> f.match()
                    		              .fields( "zitat" )
                    		              .matching( suchbegriff )
                    		              .fuzzy( maxEditDistance ) )
                            .fetch( maxTreffer )
                            .hits();
    }
	
}
