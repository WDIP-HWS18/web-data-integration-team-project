package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class MusicXMLFormatter extends XMLFormatter<Music> {

    ArtistXMLFormatter artistFormatter = new ArtistXMLFormatter();
    BandXMLFormatter bandFormatter = new BandXMLFormatter();

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("music");
    }

    @Override
    public Element createElementFromRecord(Music record, Document doc) {
        Element music = doc.createElement("music");

        music.appendChild(createTextElement("id", record.getIdentifier(), doc));

        music.appendChild(createTextElement("songName",
                record.getSongName(),
                doc));
        music.appendChild(createTextElement("songGenre",
                record.getSongGenre(),
                doc));
        music.appendChild(createTextElement("songYear",
                record.getSongYear(),
                doc));
        music.appendChild(createTextElement("albumName",
                record.getAlbumName(),
                doc));
        music.appendChild(createTextElement("lyrics",
                record.getLyrics(),
                doc));

        music.appendChild(createArtistsElement(record, doc));
        music.appendChild(createBandElement(record, doc));

        return music;
    }

    protected Element createTextElementWithProvenance(String name, String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

    protected Element createArtistsElement(Music record, Document doc) {
        Element artistRoot = artistFormatter.createRootElement(doc);     
        for (Artist a : record.getArtists()) {
                artistRoot.appendChild(artistFormatter.createElementFromRecord(a, doc));
       }
        return artistRoot;
    }

    protected Element createBandElement(Music record, Document doc) {
        Element bandRoot = bandFormatter.createRootElement(doc);    
        for (Band a : record.getBands()) {
                bandRoot.appendChild(bandFormatter.createElementFromRecord(a, doc));
        }
        return bandRoot;
    }

}
