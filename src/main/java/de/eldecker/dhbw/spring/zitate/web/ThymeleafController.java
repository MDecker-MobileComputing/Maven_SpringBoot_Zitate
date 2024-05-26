package de.eldecker.dhbw.spring.zitate.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller-Klasse für Thymeleaf-Templates. Jede Mapping-Methode
 * gibt den String mit dem Namen der Template-Datei (ohne Datei-Endung)
 * zurück, die angezeigt werden soll.
 */
@Controller
@RequestMapping( "/app/" )
public class ThymeleafController {

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

		model.addAttribute( "suchbegriff", suchbegriffTrimmed );

		return "suche-ergebnis";
	}

}
