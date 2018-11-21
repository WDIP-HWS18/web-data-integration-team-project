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

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;


    public class Artist extends AbstractRecord<Attribute> implements Serializable {
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

        //我们能不能只留下songName和artistName? 而且在music里面，其实是包含了artistName的
        /*
         * <songName>Ego Remix</songName>
         * <artist>
           * <artistName>Beyonce Knowles</artistName>
         * </artist>
         */

        private static final long serialVersionUID = 1L;//这行是什么意思？
        private String songName;
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

        public String getSongName() {
            return songName;
        }

        public void setSongName(String songName) {
            this.songName = songName;
        }


        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            int result = 31 + ((artistName == null) ? 0 : artistName.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
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
        public static final Attribute SONGNAME = new Attribute("songName");

        /* (non-Javadoc)
         * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
         */
        @Override
        public boolean hasValue(Attribute attribute) {
            if(attribute==ARTISTNAME)
                return artistName!=null;
            else if(attribute==SONGNAME)
                return songName!=null;
            return false;
        }
    }