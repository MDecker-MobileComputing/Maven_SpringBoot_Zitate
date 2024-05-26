package de.eldecker.dhbw.spring.zitate.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.eldecker.dhbw.spring.zitate.db.FuzzySuche;
import de.eldecker.dhbw.spring.zitate.db.ZitatEntity;
import de.eldecker.dhbw.spring.zitate.db.ZitateRepo;
import de.eldecker.dhbw.spring.zitate.helferlein.ZitateException;


/**
 * Controller-Klasse für Thymeleaf-Templates. Jede Mapping-Methode
 * gibt den String mit dem Namen der Template-Datei (ohne Datei-Endung)
 * zurück, die angezeigt werden soll.
 */
@Controller
@RequestMapping( "/app/" )
public class ThymeleafController {

    private static final Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

    /** Repo-Bean für Fuzzy-Suche. */
    private final FuzzySuche _fuzzySuche;

    /** Repo-Bean für "normalen" Zugriff auf Datenbanktabelle mit Zitaten. */
    private final ZitateRepo _zitateRepo;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public ThymeleafController ( FuzzySuche fuzzySuche,
                                 ZitateRepo zitateRepo ) {

        _fuzzySuche = fuzzySuche;
        _zitateRepo = zitateRepo;
    }


    /**
     * Exception-Handler.
     *
     * @param exception Exception-Objekt, das von einer Controller-Methode in dieser
     *                  Klasse geworfen wurde. Die darin enthaltene Fehlermeldung
     *                  wird auf den Logger geschrieben und auch auf der Fehlerseite
     *                  angezeigt.
     *
     * @param model Objekt für Platzhalterwerte in Template.
     *
     * @return Name der Template-Datei "such-fehler.html" ohne Datei-Endung
     */
    @ExceptionHandler(Exception.class)
    public String exceptionBehandeln( Exception ex, Model model ) {

        final String fehlerText = ex.getMessage();
        LOG.error( "Exception bei Suche aufgetreten: " + fehlerText );

        model.addAttribute( "fehlermeldung", fehlerText );

        return "suche-fehler";
    }


	/**
	 * Controller-Methode für Suche.
	 *
	 * @param suchbegriff In Suchformular eingegebener Suchbegriff; obligatorisch;
	 *                    muss nach Trimming mindestens drei Zeichen lang sein.
	 *
	 * @param maxTreffer  Maximale Anzahl der anzuzeigenden Treffer (optionaler Parameter);
	 *                    Default-Wert ist 10, darf nicht kleiner als 1 und nicht größer als 500 sein.
	 *
	 * @param fuzzyMaxEditDistance Parameter für Steuerung der Fuzzy-Suche (optionaler Parameter);
	 *                             je höher der Wert, desto ungenauere Treffer werden zurückgeliefert;
	 *                             bei 0 ist Fuzzy-Suche ausgeschaltet;
	 *                             Werte echt-kleiner 0 und echt-größer 10 sind nicht zulässig.
	 *                             Der Default-Wert ist 2.
	 *
	 * @param model Objekt für Platzhalterwerte in Template.
	 *
	 * @return Name der Template-Datei "such-ergebnis.html" ohne Datei-Endung (auch wenn nichts gefunden wurde)
	 *
	 * @throws ZitateException Ungültiger Parameter übergeben: {@code suchbegriff} zu kurz oder Wert für
	 *                         {@code maxTreffer} und/oder {@code fuzzyMaxEditDistance} nicht im zulässigen
	 *                         Bereich; werden von Methode {@link #exceptionBehandeln(Exception, Model)}
	 *                         verarbeitet.
	 */
	@GetMapping( "/suche" )
	public String suche( @RequestParam(value = "suchbegriff", required = true                      ) String suchbegriff      ,
	                     @RequestParam(value = "maxTreffer" , required = false, defaultValue = "10") int maxTreffer          ,
						 @RequestParam(value = "fuzziness"  , required = false, defaultValue = "2" ) int fuzzyMaxEditDistance,
			             Model model ) throws ZitateException {

		final String suchbegriffTrimmed = suchbegriff.trim();
		if ( suchbegriffTrimmed.length() < 4 ) {

		    throw new ZitateException( "Suchbegriff \"" + suchbegriffTrimmed + "\" hat weniger als drei Buchstaben." );

		}
		if ( maxTreffer < 1 || maxTreffer > 500 ) {

		    throw new ZitateException(
		            "Parameter \"maxTreffer\" liegt nicht im zulässigen Bereich von 1-500: " + maxTreffer );
		}
        if ( fuzzyMaxEditDistance < 0 || fuzzyMaxEditDistance > 10 ) {

            throw new ZitateException(
                    "Parameter \"fuzzyMaxEditDistance\" liegt nicht im zulässigen Bereich von 0-10: " +
                    fuzzyMaxEditDistance );
        }

		final List<ZitatEntity> ergebnisListe =
					_fuzzySuche.searchFuzzy( suchbegriffTrimmed, maxTreffer, 2 );

		model.addAttribute( "suchbegriff"  , suchbegriffTrimmed );
		model.addAttribute( "ergebnisListe", ergebnisListe      );

		return "suche-ergebnis";
	}


	/**
	 * Controller-Methode für Seite, die ein zufällig ausgewähltes Zitat anzeigt.
	 *
	 * @param model Objekt für Platzhalterwerte in Template.
	 *
	 * @return Name der Template-Datei "zitat-zufall.html" ohne Datei-Endung
	 */
	@GetMapping( "/zufall" )
	public String zufaelligesZitat( Model model ) {

	    final Optional<ZitatEntity> zitatOptional = _zitateRepo.getZufallsZitat();

	    String zitat = "";
	    int    id    = -1;

	    if ( zitatOptional.isEmpty() ) {

	        zitat = "Kein zufälliges Zitat gefunden (ist die Datenbank leer?)";

	    } else {

	        final ZitatEntity zufallsEntity = zitatOptional.get();
	        
	        zitat = zufallsEntity.getZitat();
	        id    = (int) zufallsEntity.getId().intValue();
	    }

		final int anzahlZitate = (int) _zitateRepo.count();

	    model.addAttribute( "zitat_text"   , zitat        );
	    model.addAttribute( "zitat_id"     , id           );
		model.addAttribute( "anzahl_zitate", anzahlZitate );

	    return "zitat-zufall";
	}

}
