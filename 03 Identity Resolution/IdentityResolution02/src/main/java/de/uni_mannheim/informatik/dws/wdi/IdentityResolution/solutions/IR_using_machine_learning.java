package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.solutions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.ErrorAnalysis;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDirectorComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDirectorComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDirectorComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MusicBlockingKeyBySongNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicAlbumNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicAlbumNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicAlbumNameComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongGenreComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongGenreComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongGenreComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongNameComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongYearComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MusicSongYearComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.MusicXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
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

public class IR_using_machine_learning {
	
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
		HashedDataSet<Music, Attribute> lyrics14 = new HashedDataSet<>();
		new MusicXMLReader().loadFromXML(new File("data/input/lyrics14.xml"), "/music/music", lyrics14);
		HashedDataSet<Music, Attribute> million14 = new HashedDataSet<>();
		new MusicXMLReader().loadFromXML(new File("data/input/million14.xml"), "/music/music", million14);

		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic";
		WekaMatchingRule<Music, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		//Write debug results to file
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);
		
		// add comparators
		matchingRule.addComparator(new MusicSongNameComparatorJaccard());
		matchingRule.addComparator(new MusicSongNameComparatorLevenshtein());
		matchingRule.addComparator(new MusicSongNameComparatorLowerCaseJaccard());
		matchingRule.addComparator(new MusicSongYearComparatorJaccard());
		matchingRule.addComparator(new MusicSongYearComparatorLevenshtein());
		matchingRule.addComparator(new MusicSongGenreComparatorJaccard());
		matchingRule.addComparator(new MusicSongGenreComparatorLevenshtein());
		matchingRule.addComparator(new MusicSongGenreComparatorLowerCaseJaccard());
		matchingRule.addComparator(new MusicAlbumNameComparatorJaccard());
		matchingRule.addComparator(new MusicAlbumNameComparatorLevenshtein());
		matchingRule.addComparator(new MusicAlbumNameComparatorLowerCaseJaccard());
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		// gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors.csv"));
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));

		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<Music, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(lyrics14, million14, null, matchingRule, gsTraining);
		System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
		StandardRecordBlocker<Music, Attribute> blocker = new StandardRecordBlocker<Music, Attribute>(new MusicBlockingKeyBySongNameGenerator());
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Music, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Music, Attribute>> correspondences = engine.runIdentityResolution(
			lyrics14, million14, null, matchingRule,
				blocker);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File("data/goldstandard/gs_lyrics_million_2atts1_test.csv"));

		System.out.println("*\n*\tRunning global matching\n*");

		// Create a top-1 global matching
		//  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

		// Alternative: Create a maximum-weight, bipartite matching
		MaximumBipartiteMatchingAlgorithm<Music,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
		maxWeight.run();
		correspondences = maxWeight.getResult();

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Music, Attribute> evaluator = new MatchingEvaluator<Music, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);
		
		// print the evaluation result
		System.out.println("lyrics14 <-> million14");
		System.out.println(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest.getF1()));


		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/lyrics14_2_million14_correspondences.csv"), correspondences);
		

		
    }
}
