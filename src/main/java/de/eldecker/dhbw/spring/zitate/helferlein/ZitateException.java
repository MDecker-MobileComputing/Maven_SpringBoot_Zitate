package de.eldecker.dhbw.spring.zitate.helferlein;


/**
 * Applikations-spezifische Exception-Klasse.
 */
@SuppressWarnings("serial")
public class ZitateException extends Exception {

    /**
     * Exception unter Angabe der Fehlerbeschreibung erzeugen.
     * 
     * @param fehlertext Beschreibung Fehler
     */
    public ZitateException( String fehlertext ) {
        
        super( fehlertext );
    }
}
