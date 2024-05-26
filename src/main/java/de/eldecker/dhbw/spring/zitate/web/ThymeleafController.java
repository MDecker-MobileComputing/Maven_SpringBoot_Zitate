package de.eldecker.dhbw.spring.zitate.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.eldecker.dhbw.spring.zitate.db.FuzzySuche;
import de.eldecker.dhbw.spring.zitate.db.ZitatEntity;


/**
 * Controller-Klasse für Thymeleaf-Templates. Jede Mapping-Methode
 * gibt den String mit dem Namen der Template-Datei (ohne Datei-Endung)
 * zurück, die angezeigt werden soll.
 */
@Controller
@RequestMapping( "/app/" )
public class ThymeleafController {

    @Autowired
    private FuzzySuche _fuzzySuche;
    
    
	/**
	 * Controller-Methode für Suche.
	 *
	 * @param suchbegriff In Suchformular eingegebener Suchbegriff; obligatorisch.
	 *
	 * @param model Objekt für Platzhalterwerte in Template.
	 *
	 * @return Name der Template-Datei "such-ergebnis.html" ohne Endung.
	 */
	@GetMapping( "/suche" )
	public String suche( @RequestParam(value = "suchbegriff", required = true ) String suchbegriff,
			             Model model ) {

		final String suchbegriffTrimmed = suchbegriff.trim();

		final List<ZitatEntity> ergebnisListe = _fuzzySuche.searchFuzzy( suchbegriffTrimmed, 10, 2 );
		
		model.addAttribute( "suchbegriff"  , suchbegriffTrimmed );
		model.addAttribute( "ergebnisListe", ergebnisListe      );

		return "suche-ergebnis";
	}

}
