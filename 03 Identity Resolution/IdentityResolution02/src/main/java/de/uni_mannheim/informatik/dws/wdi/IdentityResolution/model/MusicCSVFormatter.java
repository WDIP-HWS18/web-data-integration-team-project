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

import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVDataSetFormatter;


public class MusicCSVFormatter extends CSVDataSetFormatter<Music, Attribute> {

    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.model.io.CSVDataSetFormatter#getHeader(de.uni_mannheim.informatik.wdi.model.DataSet)
     */
    @Override
    public String[] getHeader(List<Attribute> orderedHeader) {
        return new String[] { "id", "songName", "songYear", "artistName", "songGenre", "lyrics", "albumName",
                "songAward", "bandName", "bandActiveYear", "bandGenre" };
    }



    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.model.io.CSVDataSetFormatter#format(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.DataSet)
     */
    @Override
    public String[] format(Music record, DataSet<Music, Attribute> dataset, List<Attribute> orderedHeader) {
        return new String[] {
                record.getIdentifier(),
                record.getSongName(),
                record.getSongYear(),
                record.getArtistName(),
                record.getSongGenre(),
                record.getLyrics(),
                record.getAlbumName(),
                record.getSongAward(),
                record.getBandName(),
                record.getBandActiveYear(),
                record.getBandGenre()
        };
    }

}
