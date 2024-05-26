package de.eldecker.dhbw.spring.zitate.db;

import jakarta.persistence.EntityManager;

import java.util.List;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class FuzzySuche {

	@Autowired
	private EntityManager _entityManager;

	/**
	 * Methode nach:
	 * https://www.springcloud.io/post/2022-04/spring-boot-hibernate-search/
	 * 
	 * @param suchbegriff
	 * @param maxTreffer
	 * @param maxEditDistance
	 * @return
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
