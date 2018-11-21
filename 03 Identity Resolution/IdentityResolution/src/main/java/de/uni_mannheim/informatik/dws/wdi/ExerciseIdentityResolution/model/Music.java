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

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

/**
 * A {@link AbstractRecord} representing a music.
 *
 *
 */
public class Music implements Matchable {
    /*
     * example entry <music>
     * <id>mil_0001</id>
     * <songName>Ego Remix</songName>
     * <songYear>2009</songYear>
     * <artist>
       * <artistName>Beyonce Knowles</artistName>
     * </artist>
     * <songGenre>Pop</songGenre>
     * <lyrics>Oh baby, how you doing?…………………………………………</lyrics>
     * <albumName>Empire</albumName>
     * <songAward>Mtv Video Music Awards</songAward>
     * <band>
       * <bandName>TFBOYS</bandName>
       * <bandActiveYear>1993</bandActiveYear>
       * <bandGenre>Rock, Pop</bandGenre>
     * </band>
     * </music>
     */

    protected String id;
    protected String provenance;
    private String songName;
    private String songYear;
    //private LocalDateTime songYear;
    private String artistName;
    private String songGenre;
    private String lyrics;
    private String albumName;
    private String songAward;
    private String bandName;
    private String bandActiveYear;
    //private LocalDateTime bandActiveYear;
    private String bandGenre;

    public Music(String identifier, String provenance) {
        id = identifier;
        this.provenance = provenance;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongYear() {
        return songYear;
    }

    public void setSongYear(String songYear) {
        this.songYear = songYear;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public void setSongGenre(String songGenre) {
        this.songGenre = songGenre;
    }

    public String getSongAward() {
        return songAward;
    }

    public void setSongAward(String songAward) {
        this.songAward = songAward;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getBandActiveYear() {
        return bandActiveYear;
    }

    public void setBandActiveYear(String bandActiveYear) {
        this.bandActiveYear = bandActiveYear;
    }

    public String getBandGenre() {
        return bandGenre;
    }

    public void setBandGenre(String bandGenre) {
        this.bandGenre = bandGenre;
    }



    @Override
    public String toString() {
        return String.format("[Music %s: %s / %s/ %s/ %s/ %s/ %s/ %s/ %s/ %s/ %s]", getIdentifier(), getSongName(),
                getArtistName(),getSongGenre(),getLyrics(),getAlbumName(),getSongAward(),getBandName(),getBandActiveYear(),
                getBandGenre());
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Music){
            return this.getIdentifier().equals(((Music) obj).getIdentifier());
        }else
            return false;
    }



}
