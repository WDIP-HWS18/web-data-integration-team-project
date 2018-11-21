package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.BlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class MusicBlockingKeyByAlbumNameGenerator extends
		RecordBlockingKeyGenerator<Music, Attribute> {

	private static final long serialVersionUID = 1L;

	@Override
	public void generateBlockingKeys(Music record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Music>> resultCollector) {

		String[] tokens  = record.getAlbumName().split(" ");

		String blockingKeyValue = "";

		for(int i = 0; i <= 2 && i < tokens.length; i++) {
			blockingKeyValue += tokens[i].substring(0, Math.min(2,tokens[i].length())).toUpperCase();
		}

		resultCollector.next(new Pair<>(blockingKeyValue, record));
	}

}