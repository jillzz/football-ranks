package ranking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import weka.core.matrix.Matrix;

public class Main {
		
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
		MatchUpGraph graph = new MatchUpGraph();
		try {
			graph.readDataIn();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// i loses from j, edge weight = loss ratio of i
		graph.buildGraph(LinkWeighter.GOALS_DECEDED_RATIO);
		
		Matrix ranks = PageRanker.pagerank(graph.getAdj(), 0.2);
		ArrayList<Pair> pRank = new ArrayList<Pair>();
		Main m = new Main();
		for (int i = 0; i < ranks.getRowDimension(); i++)
			pRank.add(m.new Pair(i, ranks.get(i, 0)));
		Collections.sort(pRank);
		Collections.reverse(pRank);
		
		try {
			printRanks (pRank, graph);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private static void printRanks(ArrayList<Pair> pRank, MatchUpGraph m) throws SQLException {
		String query = "SELECT country FROM football.countries WHERE (id = %d);";
		
		ResultSet res;
		String country;
		System.out.println("PAGERANKS:\n");
		int i = 0;
		for (Pair p : pRank) {
			res = MatchUpGraph.db.fetchExecute(String.format(query, m.id[p.id]));
			res.first();
			country = res.getString("country");
			System.out.printf("%3d%30s\t%f\n", i++, country, p.rank);
		}
		
	}

}
