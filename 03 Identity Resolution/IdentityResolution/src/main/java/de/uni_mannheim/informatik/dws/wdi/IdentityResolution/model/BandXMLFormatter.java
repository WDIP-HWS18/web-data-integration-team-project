package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class BandXMLFormatter extends XMLFormatter<Band> {

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("bands");
    }

    @Override
    public Element createElementFromRecord(Band record, Document doc) {
        Element band = doc.createElement("band");

        band.appendChild(createTextElement("bandName", record.getBandName(), doc));

        return band;
    }

}
