package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class ArtistXMLReader extends XMLMatchableReader<Artist, Attribute> {

    @Override
    public Artist createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "id");

        // create the object with id and provenance information
        Artist artist = new Artist(id, provenanceInfo);

        // fill the attributes
        artist.setArtistName(getValueFromChildElement(node, "artistName"));

        return artist;
    }

}
