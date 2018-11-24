package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class Music implements Matchable {

    protected String id;
    protected String provenance;
    private String songName;
    private String songGenre;
    private String songYear;
    private String albumName;
    private String lyrics;
    private List<Artist> artists;
    private List<Band> bands;


    public Music(String identifier, String provenance) {
        id = identifier;
        this.provenance = provenance;
        artists = new LinkedList<>();
        bands = new LinkedList<>();
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
        return this.songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongGenre() {
        return this.songGenre;
    }

    public void setSongGenre(String songGenre) {
        this.songGenre = songGenre;
    }

    public String getSongYear() {
        return this.songYear;
    }

    public void setSongYear(String songYear) {
        this.songYear = songYear;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public List<Artist> getArtists() {
        return this.artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Band> getBands() {
        return this.bands;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }


    @Override
	public String toString() {
		return String.format("[Music %s: %s / %s / %s]", getIdentifier(), getSongName(), getAlbumName(), getSongYear().toString());
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
