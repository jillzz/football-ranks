package ranking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import crawler.Database;

import weka.core.matrix.Matrix;

public class MatchUpGraph {
	public static Database db = new Database();                           // database               
	private static final int n = 216;                                     // number of countries
	
	private HashMap<Pair, PairStats> matchupData;                         // matches data for every two national teams
	private Matrix adj;                                                   // adjacency matrix of the graph
	private HashMap<Integer, Integer> idToNode;                           // database id to graph node's id mapping
	public int [] id;                                                     // graph node's id to database id mapping
	
	
	/**
	 * Constructor
	 */
	public MatchUpGraph() {
		this.adj = new Matrix(n, n);
		this.matchupData = new HashMap<Pair, PairStats>();
		this.idToNode = new HashMap<Integer, Integer>();
		id = new int [n];
	}

	
	/**
	 * Read in the data from the database
	 * 
	 * @throws SQLException
	 */
	public void readDataIn () throws SQLException {
		String querryAll = "SELECT * FROM football.matches";
		ResultSet result = db.fetchExecute(querryAll);
		
		int team1, team2, games, team1Wins, draws, team2Wins, team1Goals, team2Goals;
		int node = 0;
		
		while (result.next()) {
			team1 = result.getInt("team1");
			if(!idToNode.containsKey(team1)) {
				id[node] = team1;
				idToNode.put(team1, node++);
			}
			team2 = result.getInt("team2");
			if(!idToNode.containsKey(team2)) {
				id[node] = team2;
				idToNode.put(team2, node++);
			}
			games = result.getInt("games");
			team1Wins = result.getInt("t1_wins");
			draws = result.getInt("draws");
			team2Wins = result.getInt("t2_wins");
			team1Goals = result.getInt("t1_goals");
			team2Goals = result.getInt("t2_goals");
			
			if (games <= 1) continue;			
			
			matchupData.put(new Pair(idToNode.get(team1), idToNode.get(team2)), 
					new PairStats(games, team1Wins, draws, team2Wins, team1Goals, team2Goals));
			matchupData.put(new Pair(idToNode.get(team2), idToNode.get(team1)), 
					new PairStats(games, team2Wins, draws, team1Wins, team2Goals, team1Goals));	
		}		
	}	
	
	
	/**
	 * Build the graph based on the given weighting function
	 * 
	 * @param function
	 */
	public void buildGraph (int function) {		
		Pair p = new Pair(0, 0);
		PairStats stats;
		LinkWeighter w = new LinkWeighter(function);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				p.setTeam1(i);
				p.setTeam2(j);
				
				stats = matchupData.get(p);
				if (stats == null)
					continue;
				
				adj.set(i, j, w.weight(stats));
			}
		}
	}
	
	
	/**
	 * Returns the Adjacency Matrix
	 * 
	 * @return Matrix
	 */
	public Matrix getAdj () {
		return adj;
	}
}
