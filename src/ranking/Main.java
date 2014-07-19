package ranking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import weka.core.matrix.Matrix;

public class Main {
		
	/**
	 * Id - Pagerank pairs
	 *	
	 */
	private class Pair implements Comparable<Pair> {
		int id;
		double rank;
		
		
		public Pair(int id, double rank) {
			this.id = id;
			this.rank = rank;
		}


		@Override
		public int compareTo(Pair o) {
			if (rank > o.rank) return 1;
			if (rank < o.rank) return -1;
			return 0;
		}		
	}

	
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
		
		graph.buildGraph(LinkWeighter.LOSS_RATIO);                        // build the graph
		
		// TODO
		//System.out.println("215-th node:");
		//System.out.println(graph.id[215]);
		
		
		Matrix ranks = PageRanker.pagerank(graph.getAdj(), 0.2);          // calculate pagerank
		
		ArrayList<Pair> pRank = new ArrayList<Pair>();
		Main m = new Main();
		for (int i = 0; i < ranks.getRowDimension(); i++)
			pRank.add(m.new Pair(i, ranks.get(i, 0)));
		Collections.sort(pRank);
		Collections.reverse(pRank);                                       // sort the pageranks in descending order
		
		try {
			printRanks (pRank, graph);                                    // print the ranks
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			writeResults(pRank, graph);
		} catch (SQLException | IOException e) {
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
	private static void printRanks(ArrayList<Pair> pRank, MatchUpGraph m) throws SQLException {
		String query = "SELECT country FROM football.countries WHERE (id = %d);";
		
		ResultSet res;
		String country;
		System.out.println("*************************************************");
		System.out.println("PAGERANKS:");
		System.out.println("*************************************************");
		int i = 0;
		for (Pair p : pRank) {
			res = MatchUpGraph.db.fetchExecute(String.format(query, m.id[p.id]));
			res.first();
			country = res.getString("country");
			System.out.printf("%3d%30s\t%f\n", ++i, country, p.rank);
		}
		
	}
	
		
	/**
	 * Write results to a file
	 * 
	 * @param pRank
	 * @param m
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void writeResults (ArrayList<Pair> pRank, MatchUpGraph m) throws SQLException, IOException {
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
		for (Pair p : pRank) {
			res = MatchUpGraph.db.fetchExecute(String.format(query, m.id[p.id]));
			res.first();
			country = res.getString("country");
			pw.printf("%3d%30s\t%f\n", ++i, country, p.rank);
		}
		
		pw.println("\n\n");
		pw.close();
		in.close();
	}

}
