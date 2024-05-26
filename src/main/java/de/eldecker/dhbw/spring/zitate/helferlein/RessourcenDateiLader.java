package de.eldecker.dhbw.spring.zitate.helferlein;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


/**
 * Hilfs-Bean zum Laden der Textdatei mit den Zitaten, die in die
 * Datenbank geladen werden.
 */
@Component
public class RessourcenDateiLader {

    private static Logger LOG = LoggerFactory.getLogger( RessourcenDateiLader.class );

    /** Bean zum Laden von Ressourcen-Dateien. */
    private final ResourceLoader _resourceLoader;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public RessourcenDateiLader( ResourceLoader resourceLoader ) {

        _resourceLoader = resourceLoader;
    }


    /**
     * Zeilen einer Textdatei aus dem Ressourcen-Ordner laden.
     *
     * @param pfad Pfad zur Datei relativ im Ressourcen-Ordner, z.B. {@code "zitate.txt"}
     *
     * @return Liste der eingelesenen Zeilen; kann leer sein, aber nicht {@code null}.
     */
    public List<String> ladeZeilenAusRessourcenDatei( String pfad ) {

        final String resourceLocation = "classpath:" + pfad;

        final Resource resource = _resourceLoader.getResource( resourceLocation );

        try {

            final Path path = resource.getFile().toPath();

            final List<String> stringListe = Files.readAllLines( path, UTF_8 ); // throws IOException

            LOG.info( "Anzahl der Zeilen aus Ressourcen-Datei \"{}\" eingelesen: {}",
                      path, stringListe.size() );

            return stringListe;
        }
        catch ( IOException ex ) {

            LOG.error( "Fehler beim Versuch die Ressourcendatei \"{}\" einzulesen: ",
                       pfad, ex );

            return emptyList();
        }
    }

}
