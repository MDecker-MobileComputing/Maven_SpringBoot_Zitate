package de.eldecker.dhbw.spring.zitate.logik;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.zitate.db.ZitatEntity;
import de.eldecker.dhbw.spring.zitate.db.ZitateRepo;
import de.eldecker.dhbw.spring.zitate.helferlein.RessourcenDateiLader;


/**
 * Service-Bean, die unmittelbar nach Hochfahren der Spring-Boot-Anwendung überprüft,
 * ob Zitate in die Datenbank geladen werden müssen.
 * <br><br>
 *
 * Beispiel für Format von Zitatdatei:
 * <pre>
 * "Laugh and the world laughs with you, snore and you
 * sleep alone." -- Anthony Burgess
 * .
 * "A successful man is one who makes more money than his
 * wife can spend. A successful woman is one who can
 * find such a man." -- Lana Turner
 * .
 * "Roses are red, violets are blue, I'm schizophrenic,
 * and so am I."
 * -- Oscar Levant
 * </pre>
 * <ul>
 * <li>
 *   Die einzelnen Zitate können über mehrere Zeilen gehen,
 *   wobei jede Zeile am Ende ein Leerzeichen hat.
 * </li>
 * <li>
 *   Wenn eine nur aus einem Punkt bestehende Zeile kommt, dann
 *   ist das Ende eines Zitats erreicht.
 * </li>
 * <li>
 *   Die Textdatei muss in UTF8 kodiert sein.
 * </li>
 * </ul>
 * <br><br>
 *
 * Zitatdatei mit ca. 1.900 Zitaten in diesem Format von <i>Rudy Velthuis</i>:
 * <ul>
 *   <li><a href="http://rvelthuis.de/zips/quotes.txt">http://rvelthuis.de/zips/quotes.txt</a></li>
 *   <li><a href="https://blogs.embarcadero.com/my-quotes-txt-file/">Blog-Artikel zu dieser Datei</a></li>
 * </ul>
 *
 */
@Service
public class DatenImporterRunner implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger( DatenImporterRunner.class );

	/** Repo-Bean für Zugriff auf Datenbanktabelle mit Zitaten. */
	private final ZitateRepo _zitateRepo;

	/** Hilfs-Bean für zeileweises Einlesen der Textdatei mit den Zitaten aus Ressourcen-Ordner. */
	private final RessourcenDateiLader _dateiLader;


	/**
	 * Konstruktor für <i>Dependency Injection</i>.
	 */
	@Autowired
	public DatenImporterRunner( ZitateRepo           zitateRepo,
	                            RessourcenDateiLader dateiLader ) {

		_zitateRepo = zitateRepo;
		_dateiLader = dateiLader;
	}


	/**
	 * Methode wird unmittelbar nach Hochfahren der Spring-Boot-Anwendung (wenn alles
	 * initialisiert ist) aufgerufen. Sie überprüft dann, ob schon Zitate in der Datenbank
	 * sind. Wenn noch überhaupt kein Zitat in der Datenbank ist, dann werden welche
	 * aus einer Ressourcen-Datei geladen.
	 *
	 * @param args Wird nicht ausgewertet
	 */
	@Override
	public void run( ApplicationArguments args ) throws Exception {

		final long anzahlZitateAlt = _zitateRepo.count();
		if ( anzahlZitateAlt > 0 ) {

			LOG.info( "Es sind schon {} Zitate in der Datenbank, deshalb werden keine Daten importiert.",
					  anzahlZitateAlt );
		} else {

			LOG.info( "Noch keine Zitate in der Datenbank, lade jetzt welche ..." );

			final List<String> zeilenListe = _dateiLader.ladeZeilenAusRessourcenDatei( "zitate.txt" );
			if ( zeilenListe.isEmpty() ) {

			    LOG.error( "Datei mit Zitaten enthielt 0 Zeilen." );

			} else {

			    final List<String> zitatListe = zeilenZuZitate( zeilenListe );

			    final List<ZitatEntity> zitatEntityListe = zitatListe.stream()
			                                                         .map( zitatString -> new ZitatEntity( zitatString ) )
			                                                         .toList();

			    LOG.info( "Anzahl Zitate gefunden: {}", zitatListe.size() );

			    _zitateRepo.saveAll( zitatEntityListe );
			}


			final long anzahlZitateNeu = _zitateRepo.count();
			LOG.info( "Es wurden {} Zitate in die Datenbank geladen.", anzahlZitateNeu );
		}
	}


	/**
	 * In der Ressourcen-Datei sind die Zitate auf mehrere Zeilen verteilt. Diese Methode
	 * fasst die Zeilen eines Zitats zu einem String zusammen. Dabei wird ausgewertet,
	 * dass zwischen zwei Zitaten eine einzelne Zeile mit einem Punkt steht.
	 *
	 * @param zeilenListe Liste der einzelnen Zeilen aus der Ressourcen-Datei; muss
	 *                    mindestens eine Zeile enthalten.
	 *
	 * @return Liste mit einem Zitat pro Zeile; kann 0 Einträge enthalten, aber nicht
	 *         {@code null} sein
	 */
	private List<String> zeilenZuZitate( List<String> zeilenListe ) {

	    final int anzahlZitateGeschaetzt = zeilenListe.size() / 2;
	    final List<String> ergebnisListe = new ArrayList<>( anzahlZitateGeschaetzt );

	    String zitat = "";
	    for ( String zeile: zeilenListe ) {

	        if ( zeile.trim().equals( ".") ) {

	            ergebnisListe.add( zitat );
	            zitat = "";

	        } else {

	            zitat = zitat + zeile;
	        }
	    }

	    if ( !zitat.isBlank() ) {

	        ergebnisListe.add( zitat );
	    }

	    return ergebnisListe;
	}

}
