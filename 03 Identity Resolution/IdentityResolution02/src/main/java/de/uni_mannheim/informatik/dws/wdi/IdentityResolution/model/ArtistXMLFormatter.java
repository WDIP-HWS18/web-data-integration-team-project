package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class ArtistXMLFormatter extends XMLFormatter<Artist> {

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("artists");
    }

    @Override
    public Element createElementFromRecord(Artist record, Document doc) {
        Element artist = doc.createElement("artist");

        artist.appendChild(createTextElement("artistName", record.getArtistName(), doc));

        return artist;
    }

}
