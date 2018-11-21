package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.solutions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.ErrorAnalysis;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;
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
		HashedDataSet<Movie, Attribute> dataAcademyAwards = new HashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/input/academy_awards.xml"), "/movies/movie", dataAcademyAwards);
		HashedDataSet<Movie, Attribute> dataActors = new HashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/input/actors.xml"), "/movies/movie", dataActors);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_academy_awards_2_actors_test.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Movie, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		
		// add comparators
		matchingRule.addComparator(new MovieDateComparator2Years(), 0.3);
		matchingRule.addComparator(new MovieTitleComparatorJaccard(), 0.7);
		
		// create a blocker (blocking strategy)
		Blocker<Movie,Attribute,Movie,Attribute> blocker = new StandardRecordBlocker<>(new MovieBlockingKeyByTitleGenerator());
		
		System.out.println("*\n*\tStandard Blocker: by title\n*");
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);

		System.out.println("*\n*\tStandard Blocker: by decade\n*");
		blocker = new StandardRecordBlocker<>(new MovieBlockingKeyByDecadeGenerator());
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);

		System.out.println("*\n*\tStandard Blocker: by year\n*");
		blocker = new StandardRecordBlocker<>(new MovieBlockingKeyByYearGenerator());
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);

		System.out.println("*\n*\tSorted-Neighbourhood Blocker: by year\n*");
		blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByYearGenerator(), 30);
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);

		System.out.println("*\n*\tSorted-Neighbourhood Blocker: by title\n*");
		blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 30);
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);

		System.out.println("*\n*\tNo Blocker\n*");
		blocker = new NoBlocker<>();
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);
	}
	
	protected static void testBlocker(Blocker<Movie,Attribute,Movie,Attribute> blocker, DataSet<Movie,Attribute> ds1, DataSet<Movie,Attribute> ds2, MatchingRule<Movie,Attribute> rule, MatchingGoldStandard gsTest) {
		// Initialize Matching Engine
		MatchingEngine<Movie, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Movie, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null, rule,blocker);

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Movie, Attribute> evaluator = new MatchingEvaluator<Movie, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
			gsTest);

		// print the evaluation result
		System.out.println("Academy Awards <-> Actors");
		System.out.println(String.format(
			"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
			"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
			"F1: %.4f",perfTest.getF1()));
	}
}
