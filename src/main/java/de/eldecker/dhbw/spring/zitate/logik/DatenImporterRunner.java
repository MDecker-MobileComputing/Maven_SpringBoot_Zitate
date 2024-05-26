package de.eldecker.dhbw.spring.zitate.logik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.zitate.db.ZitatEntity;
import de.eldecker.dhbw.spring.zitate.db.ZitateRepo;


/**
 * Service-Bean, die unmittelbar nach Hochfahren der Spring-Boot-Anwendung überprüft, 
 * ob Zitate in die Datenbank geladen werden müssen.
 */
@Service
public class DatenImporterRunner implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger( DatenImporterRunner.class );
	
	/** Repo-Bean für Zugriff auf Datenbanktabelle mit Zitaten. */
	private ZitateRepo _repo;
	
	
	/**
	 * Konstruktor für <i>Dependency Injection</i>.
	 */
	@Autowired
	public DatenImporterRunner( ZitateRepo zitateRepo ) {
	
		_repo = zitateRepo;
	}
	
	
	/**
	 * Methode wird unmittelbar nach Hochfahren der Spring-Boot-Anwendung (wenn alles
	 * initialisiert ist) aufgerufen. Sie überprüft dann, ob schon Zitate in der Datenbank
	 * sind. Wenn noch überhaupt kein Zitat in der Datenbank ist, dann werden welche
	 * geladen.
	 * 
	 * @param args Wird nicht ausgewertet 
	 */
	@Override
	public void run( ApplicationArguments args ) throws Exception {

		final long anzahlZitateAlt = _repo.count();
		if ( anzahlZitateAlt > 0 ) {
			
			LOG.info( "Es sind schon {} Zitate in der Datenbank, deshalb werden keine Daten importiert." );
			
		} else {
			
			LOG.info( "Noch keine Zitate in der Datenbank, lade jetzt welche ..." );
			
			final ZitatEntity zitat1 = new ZitatEntity( "Laugh and the world laughs with you, snore and you sleep alone. -- Anthony Burgess" );
			_repo.save( zitat1 );
			
			final ZitatEntity zitat2 = new ZitatEntity( "Behind every great man is a woman rolling her eyes.. -- Jim Carrey" );
			_repo.save( zitat2 );
		
			final long anzahlZitateNeu = _repo.count();
			LOG.info( "Es wurden {} Zitate in die Datenbank geladen.", anzahlZitateNeu );
		}
	}

}
