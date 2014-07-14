package ranking;


public class LinkWeighter {
	public static final int WIN_RATIO = 1;
	public static final int LOSS_RATIO = 2;
	public static final int GOALS_DIFF_SIG = 3;
	public static final int LOSS_WIN_RATIO = 4;
	public static final int GOALS_DECEDED_RATIO = 5;

	private int function;
	
	
	public LinkWeighter(int function) {
		this.function = function;
	}
	
	
	public double weight (int team, PairStats stat) {
		switch (function) {
			case 1: return winRatio(team, stat);
			case 2: return lossRatio(team, stat);
			case 3: return goalsDiffSig(team, stat);
			case 4: return lossRatio(team, stat);
			case 5: return goalsDecededRatio(team, stat);
			default: return 0;
		}
	}
	
	
	public double winRatio (int team, PairStats stat) {
		if (team == 1)
			return stat.getTeam1Wins() / (double) stat.getGames();
		else if (team == 2)
			return  stat.getTeam2Wins() / (double) stat.getGames();
		return 0;
	}
	
	
	public double lossRatio (int team, PairStats stat) {
		if (team == 1)
			return winRatio(2, stat);
		else if (team == 2)
			return winRatio(1, stat);
		return 0;
	}
	
	
	public double goalsDiffSig (int team, PairStats stat) {
		double x;
		if (team == 1) 
			x = stat.getTeam2Goals()-stat.getTeam1Goals();
		else if (team == 2)
			x = stat.getTeam1Goals()-stat.getTeam2Goals();
		else
			x = 0;
		
		return 1.0 / (1 + Math.exp(-x));
	}	
	
	
	public double lossWinRatio (int team, PairStats stat) {
		if (team == 1) 
			return stat.getTeam2Wins() / (double) stat.getTeam1Wins();
		else if (team == 2)
			return stat.getTeam1Wins() / (double) stat.getTeam2Wins();
		return 0;		
	}
	
	
	public double goalsDecededRatio (int team, PairStats stat) {
		if (team == 1) 
			return stat.getTeam2Goals() / (double) (stat.getTeam1Goals() + stat.getTeam2Goals() + 1e-15);
		else if (team == 2)
			return stat.getTeam1Goals() / (double) (stat.getTeam1Goals() + stat.getTeam2Goals() + 1e-15);
		return 0;
	}
}
