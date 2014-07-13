package ranking;

public class PairStats {
	private short games;
	private short team1Wins;
	private short draws;
	private short team2Wins;
	private short team1Goals;
	private short team2Goals;
	
	
	public PairStats(short games, short team1Wins, short draws,
			short team2Wins, short team1Goals, short team2Goals) {
		this.games = games;
		this.team1Wins = team1Wins;
		this.draws = draws;
		this.team2Wins = team2Wins;
		this.team1Goals = team1Goals;
		this.team2Goals = team2Goals;
	}
	
	
	public PairStats() {
		
	}


	public short getGames() {
		return games;
	}


	public void setGames(short games) {
		this.games = games;
	}


	public short getTeam1Wins() {
		return team1Wins;
	}


	public void setTeam1Wins(short team1Wins) {
		this.team1Wins = team1Wins;
	}


	public short getDraws() {
		return draws;
	}


	public void setDraws(short draws) {
		this.draws = draws;
	}


	public short getTeam2Wins() {
		return team2Wins;
	}


	public void setTeam2Wins(short team2Wins) {
		this.team2Wins = team2Wins;
	}


	public short getTeam1Goals() {
		return team1Goals;
	}


	public void setTeam1Goals(short team1Goals) {
		this.team1Goals = team1Goals;
	}


	public short getTeam2Goals() {
		return team2Goals;
	}


	public void setTeam2Goals(short team2Goals) {
		this.team2Goals = team2Goals;
	}	
}
