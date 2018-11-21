package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MusicBlockingKeyByArtistNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MusicBlockingKeyBySongNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MusicSongNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MusicArtistNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MusicXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_linear_combination
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
        HashedDataSet<Music, Attribute> dataSong = new HashedDataSet<>();
        new MusicXMLReader().loadFromXML(new File("data/input/lyrics14.xml"), "/music/music", dataSong);
        HashedDataSet<Music, Attribute> dataArtist = new HashedDataSet<>();
        new MusicXMLReader().loadFromXML(new File("data/input/million14.xml"), "/music/music", dataArtist);

        // load the training set
        MatchingGoldStandard gsTraining = new MatchingGoldStandard();
        gsTraining.loadFromCSVFile(new File("data/goldstandard/gold_standard_million_lyrics_training.csv"));

        // create a matching rule
        LinearCombinationMatchingRule<Music, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", -1, gsTraining);

        // add comparators
        matchingRule.addComparator(new MusicArtistNameComparatorLevenshtein(), 0.5);
        matchingRule.addComparator(new MusicSongNameComparatorJaccard(), 0.5);

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Music, Attribute> blocker = new StandardRecordBlocker<Music, Attribute>(new MusicBlockingKeyBySongNameGenerator());
//		NoBlocker<Music, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Music, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MusicBlockingKeyBySongNameGenerator(), 1);
        blocker.setMeasureBlockSizes(true);
        //Write debug results to file:
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Music, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Music, Attribute>> correspondences = engine.runIdentityResolution(
                dataSong, dataArtist, null, matchingRule,
                blocker);

        // Create a top-1 global matching
        //  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

        // Alternative: Create a maximum-weight, bipartite matching
        // MaximumBipartiteMatchingAlgorithm<Music,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
        // maxWeight.run();
        // correspondences = maxWeight.getResult();

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/lyrics_million_2_actors_correspondences.csv"), correspondences);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/gs_academy_awards_2_actors_test.csv"));

        System.out.println("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Music, Attribute> evaluator = new MatchingEvaluator<Music, Attribute>();
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
