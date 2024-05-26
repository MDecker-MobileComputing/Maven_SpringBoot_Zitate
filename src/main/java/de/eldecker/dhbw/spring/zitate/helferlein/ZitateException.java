package de.eldecker.dhbw.spring.zitate.helferlein;


/**
 * Applikations-spezifische Exception-Klasse.
 */
@SuppressWarnings("serial")
public class ZitateException extends Exception {

    public ZitateException( String fehlertext ) {
        
        super( fehlertext );
    }
}
