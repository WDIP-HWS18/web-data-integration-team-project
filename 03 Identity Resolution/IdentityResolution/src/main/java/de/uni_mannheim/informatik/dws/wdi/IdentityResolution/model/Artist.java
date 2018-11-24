package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

    public class Artist extends AbstractRecord<Attribute> implements Serializable {

        private static final long serialVersionUID = 1L;
        private String artistName;

        public Artist(String identifier, String provenance) {
            super(identifier, provenance);
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        @Override
        public int hashCode() {
            int result = 31 + ((artistName == null) ? 0 : artistName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Artist other = (Artist) obj;
            if (artistName == null) {
                if (other.artistName != null)
                    return false;
            } else if (!artistName.equals(other.artistName))
                return false;
            return true;
        }

        public static final Attribute ARTISTNAME = new Attribute("artistName");

        @Override
        public boolean hasValue(Attribute attribute) {
            if(attribute==ARTISTNAME)
                return artistName!=null;
            return false;
        }
    }