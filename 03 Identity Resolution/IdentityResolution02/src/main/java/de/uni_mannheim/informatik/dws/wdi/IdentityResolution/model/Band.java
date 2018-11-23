package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

    public class Band extends AbstractRecord<Attribute> implements Serializable {

        private static final long serialVersionUID = 1L;
        private String bandName;

        public Band(String identifier, String provenance) {
            super(identifier, provenance);
        }

        public String getBandName() {
            return bandName;
        }

        public void setBandName(String bandName) {
            this.bandName = bandName;
        }

        @Override
        public int hashCode() {
            int result = 31 + ((bandName == null) ? 0 : bandName.hashCode());
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
            Band other = (Band) obj;
            if (bandName == null) {
                if (other.bandName != null)
                    return false;
            } else if (!bandName.equals(other.bandName))
                return false;
            return true;
        }

        public static final Attribute BANDNAME = new Attribute("bandName");

        @Override
        public boolean hasValue(Attribute attribute) {
            if(attribute==BANDNAME)
                return bandName!=null;
            return false;
        }
    }