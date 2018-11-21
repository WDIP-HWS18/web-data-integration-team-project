package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.solutions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.ErrorAnalysis;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MovieBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.MovieBlockingKeyByYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDirectorComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDirectorComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieDirectorComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieTitleComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieTitleComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.model.MovieXMLReader;
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
		HashedDataSet<Movie, Attribute> dataAcademyAwards = new HashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/input/academy_awards.xml"), "/movies/movie", dataAcademyAwards);
		HashedDataSet<Movie, Attribute> dataActors = new HashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/input/actors.xml"), "/movies/movie", dataActors);

		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic";
		WekaMatchingRule<Movie, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		//Write debug results to file
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);
		
		// add comparators
		matchingRule.addComparator(new MovieTitleComparatorEqual());
		matchingRule.addComparator(new MovieDateComparator2Years());
		matchingRule.addComparator(new MovieDateComparator10Years());
		matchingRule.addComparator(new MovieDirectorComparatorJaccard());
		matchingRule.addComparator(new MovieDirectorComparatorLevenshtein());
		matchingRule.addComparator(new MovieDirectorComparatorLowerCaseJaccard());
		matchingRule.addComparator(new MovieTitleComparatorLevenshtein());
		matchingRule.addComparator(new MovieTitleComparatorJaccard());
		matchingRule.addComparator(new MovieActorComparator());
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		// gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors.csv"));
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));

		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<Movie, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataAcademyAwards, dataActors, null, matchingRule, gsTraining);
		System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
		StandardRecordBlocker<Movie, Attribute> blocker = new StandardRecordBlocker<Movie, Attribute>(new MovieBlockingKeyByTitleGenerator());
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Movie, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Movie, Attribute>> correspondences = engine.runIdentityResolution(
				dataAcademyAwards, dataActors, null, matchingRule,
				blocker);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		// gsTest.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_v2.csv"));
		gsTest.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_test.csv"));

		System.out.println("*\n*\tRunning global matching\n*");

		// Create a top-1 global matching
		//  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

		// Alternative: Create a maximum-weight, bipartite matching
		MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
		maxWeight.run();
		correspondences = maxWeight.getResult();

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


		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);
		

		
    }
}
