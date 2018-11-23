package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class MusicXMLReader extends XMLMatchableReader<Music, Attribute>  {

    @Override
    protected void initialiseDataset(DataSet<Music, Attribute> dataset) {
        super.initialiseDataset(dataset);
    }

    @Override
    public Music createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "id");

        // create the object with id and provenance information
        Music music = new Music(id, provenanceInfo);

        // fill the attributes
        music.setSongName(getValueFromChildElement(node, "songName"));
        music.setSongGenre(getValueFromChildElement(node, "songGenre"));
        music.setSongYear(getValueFromChildElement(node, "songYear"));
        music.setAlbumName(getValueFromChildElement(node, "albumName"));
        music.setLyrics(getValueFromChildElement(node, "lyrics")); 

        // load the list of artists
        List<Artist> artists = getObjectListFromChildElement(node, "artists", "artist", new ArtistXMLReader(), provenanceInfo);
        music.setArtists(artists);

        // load the list of bands
        List<Band> bands = getObjectListFromChildElement(node, "bands", "band", new BandXMLReader(), provenanceInfo);
        music.setBands(bands);
        
        return music;
    }

}
