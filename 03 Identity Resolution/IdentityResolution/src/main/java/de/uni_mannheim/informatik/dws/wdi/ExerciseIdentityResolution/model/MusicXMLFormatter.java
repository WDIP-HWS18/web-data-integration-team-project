/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Music}s.
 *
 *
 */
public class MusicXMLFormatter extends XMLFormatter<Music> {

    ArtistXMLFormatter artistFormatter = new ArtistXMLFormatter();

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
        music.appendChild(createTextElement("songYear",
                record.getSongYear(),
                doc));
        music.appendChild(createTextElement("artistName",
                record.getArtistName(),
                doc));
        music.appendChild(createTextElement("songGenre",
                record.getSongGenre(),
                doc));
        music.appendChild(createTextElement("lyrics",
                record.getLyrics(),
                doc));
        music.appendChild(createTextElement("albumName",
                record.getAlbumName(),
                doc));
        music.appendChild(createTextElement("songAward",
                record.getSongAward(),
                doc));
        music.appendChild(createTextElement("bandName",
                record.getBandName(),
                doc));
        music.appendChild(createTextElement("bandActiveYear",
                record.getBandActiveYear(),
                doc));
        music.appendChild(createTextElement("bandGenre",
                record.getBandGenre(),
                doc));
        music.appendChild(createArtistsElement(record, doc));

        return music;
    }


    protected Element createTextElementWithProvenance(String name,
                                                      String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

    protected Element createArtistsElement(Music record, Document doc) {
        Element artistRoot = artistFormatter.createRootElement(doc);

//        for (Artist a : record.getArtistName()) {
//            artistRoot.appendChild(artistFormatter
//                    .createElementFromRecord(a, doc));
//        }//感觉这跟list有关

        return artistRoot;
    }

}
