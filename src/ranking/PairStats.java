package ranking;

public class PairStats {
	private int games;
	private int team1Wins;
	private int draws;
	private int team2Wins;
	private int team1Goals;
	private int team2Goals;
	
	
	public PairStats(int games, int team1Wins, int draws,
			int team2Wins, int team1Goals, int team2Goals) {
		this.games = games;
		this.team1Wins = team1Wins;
		this.draws = draws;
		this.team2Wins = team2Wins;
		this.team1Goals = team1Goals;
		this.team2Goals = team2Goals;
	}
	
	
	public PairStats() {
		
	}


	public int getGames() {
		return games;
	}


	public void setGames(int games) {
		this.games = games;
	}


	public int getTeam1Wins() {
		return team1Wins;
	}


	public void setTeam1Wins(int team1Wins) {
		this.team1Wins = team1Wins;
	}


	public int getDraws() {
		return draws;
	}


	public void setDraws(int draws) {
		this.draws = draws;
	}


	public int getTeam2Wins() {
		return team2Wins;
	}


	public void setTeam2Wins(int team2Wins) {
		this.team2Wins = team2Wins;
	}


	public int getTeam1Goals() {
		return team1Goals;
	}


	public void setTeam1Goals(int team1Goals) {
		this.team1Goals = team1Goals;
	}


	public int getTeam2Goals() {
		return team2Goals;
	}


	public void setTeam2Goals(int team2Goals) {
		this.team2Goals = team2Goals;
	}	
}
