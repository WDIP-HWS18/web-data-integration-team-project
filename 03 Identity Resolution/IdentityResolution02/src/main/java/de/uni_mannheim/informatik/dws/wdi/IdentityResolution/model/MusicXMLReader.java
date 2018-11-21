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

/**
 * A {@link XMLMatchableReader} for {@link Music}s.
 *
 *
 */
public class MusicXMLReader extends XMLMatchableReader<Music, Attribute>  {

    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
     */
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
        music.setSongYear(getValueFromChildElement(node, "songYear"));
        music.setArtistName(getValueFromChildElement(node, "artistName"));
        music.setSongGenre(getValueFromChildElement(node, "songGenre"));
        music.setLyrics(getValueFromChildElement(node, "lyrics")); 
        music.setAlbumName(getValueFromChildElement(node, "albumName"));
        music.setSongAward(getValueFromChildElement(node, "songAward"));
        music.setBandName(getValueFromChildElement(node, "bandName"));
        music.setBandActiveYear(getValueFromChildElement(node, "bandActiveYear"));
        music.setBandGenre(getValueFromChildElement(node, "bandGenre"));


        return music;
    }

}
