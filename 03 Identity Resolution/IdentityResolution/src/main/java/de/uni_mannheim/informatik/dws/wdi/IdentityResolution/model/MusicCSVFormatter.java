package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVDataSetFormatter;

public class MusicCSVFormatter extends CSVDataSetFormatter<Music, Attribute> {

    @Override
    public String[] getHeader(List<Attribute> orderedHeader) {
        return new String[] { "id", "songName", "songYear", "songGenre", "lyrics", "albumName" };
    }

    @Override
    public String[] format(Music record, DataSet<Music, Attribute> dataset, List<Attribute> orderedHeader) {
        return new String[] {
                record.getIdentifier(),
                record.getSongName(),
                record.getSongYear(),
                record.getSongGenre(),
                record.getLyrics(),
                record.getAlbumName()
        };
    }

}
