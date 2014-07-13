package ranking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import crawler.Database;

import weka.core.matrix.Matrix;

public class MatchUpGraph {
	private static final int n = 217;   // number of countries
	public static Database db = new Database();
	
	private HashMap<Pair, PairStats> matchupData;
	private Matrix adj;
	
	
	public MatchUpGraph() {
		this.adj = new Matrix(n, n);
		this.matchupData = new HashMap<Pair, PairStats>();
	}

	
	public void readDataIn () throws SQLException {
		String querryAll = "SELECT * FROM football.matches";
		ResultSet result = db.fetchExecute(querryAll);
		
		short team1, team2, games, team1Wins, draws, team2Wins, team1Goals, team2Goals;
		
		while (result.next()) {
			team1 = result.getShort("team1");
			team2 = result.getShort("team2");
			games = result.getShort("games");
			team1Wins = result.getShort("t1_wins");
			draws = result.getShort("draws");
			team2Wins = result.getShort("t2_wins");
			team1Goals = result.getShort("t1_goals");
			team2Goals = result.getShort("t2_goals");
			
			matchupData.put(new Pair(team1, team2), 
					new PairStats(games, team1Wins, draws, team2Wins, team1Goals, team2Goals));			
		}		
		
		System.out.println(matchupData.size() + " instances collected.");
	}	
	
	
	public void buildGraph (int function) {		
		Pair p = new Pair(0, 0);
		PairStats stats;
		LinkWeighter w = new LinkWeighter(function);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				p.setTeam1(i+1);
				p.setTeam2(j+1);
				
				stats = matchupData.get(p);
				if (stats == null)
					continue;
				
				adj.set(i, j, 
						w.weight(p.teamNumber(i+1), stats));
			}
		}
	}
}
