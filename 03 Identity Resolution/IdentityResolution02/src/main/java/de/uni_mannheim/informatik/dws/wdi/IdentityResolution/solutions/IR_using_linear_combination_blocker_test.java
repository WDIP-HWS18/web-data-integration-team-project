package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.solutions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.ErrorAnalysis;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.ArtistBlockingKeyByArtistNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MovieBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MovieBlockingKeyByYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MusicBlockingKeyByAlbumNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MusicBlockingKeyBySongGenreGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MusicBlockingKeyBySongNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MusicBlockingKeyBySongYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieTitleComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongGenreComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.MusicXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Group;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.processing.RecordKeyValueMapper;
import de.uni_mannheim.informatik.dws.winter.utils.Distribution;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_linear_combination_blocker_test 
{
	
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<Music, Attribute> million14 = new HashedDataSet<>();
		new MusicXMLReader().loadFromXML(new File("data/input/million14.xml"), "/music/music", million14);
		HashedDataSet<Music, Attribute> SPARQL78 = new HashedDataSet<>();
		new MusicXMLReader().loadFromXML(new File("data/input/SPARQL78.xml"), "/music/music", SPARQL78);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_million_SPARQL_2atts1_test.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Music, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		
		// add comparators
		matchingRule.addComparator(new MusicSongGenreComparatorLevenshtein(), 0.3);
		matchingRule.addComparator(new MusicSongNameComparatorJaccard(), 0.7);
		
		// create a blocker (blocking strategy)
		Blocker<Music,Attribute,Music,Attribute> blocker = new StandardRecordBlocker<>(new MusicBlockingKeyBySongNameGenerator());
		
		System.out.println("*\n*\tStandard Blocker: by title\n*");
		testBlocker(blocker, million14, SPARQL78, matchingRule, gsTest);

		System.out.println("*\n*\tStandard Blocker: by songName\n*");
		blocker = new StandardRecordBlocker<>(new MusicBlockingKeyBySongNameGenerator());
		testBlocker(blocker, million14, SPARQL78, matchingRule, gsTest);

		System.out.println("*\n*\tStandard Blocker: by songGenre\n*");
		blocker = new StandardRecordBlocker<>(new MusicBlockingKeyBySongGenreGenerator());
		testBlocker(blocker, million14, SPARQL78, matchingRule, gsTest);

		System.out.println("*\n*\tSorted-Neighbourhood Blocker: by albumName\n*");
		blocker = new SortedNeighbourhoodBlocker<>(new MusicBlockingKeyByAlbumNameGenerator(), 30);
		testBlocker(blocker, million14, SPARQL78, matchingRule, gsTest);

		System.out.println("*\n*\tSorted-Neighbourhood Blocker: by songYear\n*");
		blocker = new SortedNeighbourhoodBlocker<>(new MusicBlockingKeyBySongYearGenerator(), 30);
		testBlocker(blocker, million14, SPARQL78, matchingRule, gsTest);

		System.out.println("*\n*\tNo Blocker\n*");
		blocker = new NoBlocker<>();
		testBlocker(blocker, million14, SPARQL78, matchingRule, gsTest);
	}
	
	protected static void testBlocker(Blocker<Music,Attribute,Music,Attribute> blocker, DataSet<Music,Attribute> ds1, DataSet<Music,Attribute> ds2, MatchingRule<Music,Attribute> rule, MatchingGoldStandard gsTest) {
		// Initialize Matching Engine
		MatchingEngine<Music, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Music, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null, rule,blocker);

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Music, Attribute> evaluator = new MatchingEvaluator<Music, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
			gsTest);

		// print the evaluation result
		System.out.println("million14 <-> Music");
		System.out.println(String.format(
			"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
			"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
			"F1: %.4f",perfTest.getF1()));
	}
}
