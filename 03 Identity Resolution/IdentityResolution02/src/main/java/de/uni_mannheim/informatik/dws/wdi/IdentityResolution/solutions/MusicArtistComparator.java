package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.solutions;

import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.Artist;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.query.Q;

public class MusicArtistComparator  implements Comparator<Music, Attribute> {

	private static final long serialVersionUID = 1L;
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Music record1,
			Music record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		Set<String> actors1 = new HashSet<>();
		Set<String> actors2 = new HashSet<>();
		
		for(Artist a : record1.getArtists()) {
			actors1.add(a.getArtistName());
		}
		for(Artist a : record2.getArtists()) {
			actors2.add(a.getArtistName());
		}
		
		double similarity = Q.intersection(actors1, actors2).size() / (double)Math.max(actors1.size(), actors2.size());
		
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(actors1.toString());
			this.comparisonLog.setRecord2Value(actors2.toString());
    	
			this.comparisonLog.setSimilarity(Double.toString(similarity));
		}
		
		return similarity;
	}
	
	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}
