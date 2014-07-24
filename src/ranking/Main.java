package ranking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import weka.core.matrix.Matrix;

public class Main {
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MatchUpGraph graph = new MatchUpGraph();                          // new graph object
		try {
			graph.readDataIn();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		graph.buildGraph(LinkWeighter.LOSS_RATIO);                         // build the graph
			
		Matrix ranks = PageRanker.pagerank(graph.getAdj(), 0.2);          // calculate pagerank
		
		ArrayList<PagerankPair> pRank = new ArrayList<PagerankPair>();
		for (int i = 0; i < ranks.getRowDimension(); i++)
			pRank.add(new PagerankPair(i, ranks.get(i, 0)));
		Collections.sort(pRank);
		Collections.reverse(pRank);                                       // sort the pageranks in descending order
		
		try {
			printRanks (pRank, getCountries(pRank, graph));               // print the ranks
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		try {
			writeResults(pRank, graph);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		*/
		
		try {
			System.out.println("Error: " + getError(pRank, graph));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	
	/**
	 * Print the national teams with their pageranks
	 * 
	 * @param pRank
	 * @param m
	 * @throws SQLException
	 */
	private static void printRanks(ArrayList<PagerankPair> pRank, ArrayList<String> countries) throws SQLException {
		System.out.println("*************************************************");
		System.out.println("PAGERANKS:");
		System.out.println("*************************************************");
		int i = 0;
		for (PagerankPair p : pRank) {
			System.out.printf("%3d%30s\t%f\n", ++i, countries.get(i-1), p.rank);
		}		
	}
	
	// TODO
	public static ArrayList<String> getCountries (ArrayList<PagerankPair> pageranks, MatchUpGraph m)
			throws SQLException {
		ArrayList<String> countries = new ArrayList<String>();
		String query = "SELECT country FROM football.countries WHERE (id = %d);";
		ResultSet res;
		
		for (PagerankPair p : pageranks) {
			res = MatchUpGraph.db.fetchExecute(String.format(query, m.id[p.id]));
			res.first();
			countries.add(res.getString("country"));			
		}
		
		return countries;
	}
	
		
	/**
	 * Write results to a file
	 * 
	 * @param pRank
	 * @param m
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void writeResults (ArrayList<PagerankPair> pRank, MatchUpGraph m) throws SQLException, IOException {
		PrintWriter pw = new PrintWriter(new FileWriter ("files/results", true));
		Scanner in = new Scanner (System.in);

		String query = "SELECT country FROM football.countries WHERE (id = %d);";
		
		System.out.println("Comment:");
		pw.println(in.nextLine());
		
		ResultSet res;
		String country;
		pw.println("*************************************************");
		pw.println("PAGERANKS:");
		pw.println("*************************************************");
		int i = 0;
		for (PagerankPair p : pRank) {
			res = MatchUpGraph.db.fetchExecute(String.format(query, m.id[p.id]));
			res.first();
			country = res.getString("country");
			pw.printf("%3d%30s\t%f\n", ++i, country, p.rank);
		}
		
		pw.println("\n\n");
		pw.close();
		in.close();
	}
	
	
	public static double getError (ArrayList<PagerankPair> pageranks, MatchUpGraph graph) throws SQLException {
		final ArrayList<String> best = new ArrayList<String> (Arrays.asList(new String [] {
								"Brazil", "Germany", "England",
				                "Italy", "Argentina", "France",
				                "Yugoslavia", "Sweden", "Spain",
				                "Uruguay", "Netherlands", "Hungary", // Soviet Union
				                "Switzerland", "Austria", "Czechoslovakia",
				                "Poland", "Chile", "Peru", "Mexico", 
				                "Romania", "USA", "Portugal", "Belgium"}));
		
		final ArrayList<Integer> bestId = new ArrayList<Integer> ();
		ResultSet res;
		for (String c : best) {
			res = MatchUpGraph.db.fetchExecute(String.format(
					"SELECT id FROM football.countries WHERE (country = \"%s\");", c));
			res.first();
			bestId.add(res.getInt("id"));
		}
		
		double error = 0;
		double thisRank;
		for (Integer i : bestId) {
			thisRank = pageranks.get(pageranks.indexOf(
					new PagerankPair(graph.idToNode.get(i), 0))).rank;
			for (PagerankPair p : pageranks) {
				if (graph.id[p.id] == i)
					break;
				if (!bestId.contains(graph.id[p.id]))
					error += p.rank - thisRank;				
			}
		}
		
		return error;
	}

}
