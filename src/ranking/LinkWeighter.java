package ranking;


public class LinkWeighter {
	public static final int WIN_RATIO = 1;
	public static final int LOSS_RATIO = 2;
	public static final int GOALS_DIFF_SIG = 3;
	public static final int LOSS_WIN_RATIO = 4;
	public static final int GOALS_DECEDED_RATIO = 5;
	public static final int LOSS_WIN_RATIO_SIG = 6;

	private int function;
	
	/**
	 * Constructor
	 * 
	 * @param function
	 */
	public LinkWeighter(int function) {
		this.function = function;
	}
	
	
	/**
	 * Calculate the weight of the link from team1 to team2
	 * 
	 * @param stat
	 * @return double
	 */
	public double weight (PairStats stat) {
		switch (function) {
			case 1: return winRatio(stat);
			case 2: return lossRatio(stat);
			case 3: return goalsDiffSig(stat);
			case 4: return lossRatio(stat);
			case 5: return goalsDecededRatio(stat);
			case 6: return lossWinRatioSig(stat);
			default: return 0;
		}
	}
	
	
	/**
	 * Calculate the weight of the team1-team2 link based on team1 win ratio
	 * 
	 * @param stat
	 * @return double
	 */
	public double winRatio (PairStats stat) {
		return stat.getTeam1Wins() / (double) stat.getGames();		
	}
	
	
	/**
	 * Calculate the weight of the team1-team2 link based on team1 loss ratio
	 * 
	 * @param stat
	 * @return double
	 */
	public double lossRatio (PairStats stat) {
		double x = (stat.getTeam2Wins() / (double) stat.getGames());
		return x;
	}
	
	
	/**
	 * Calculate the weight of the team1-team2 link based on sigmoid of 
	 * team1 deceded goals difference 
	 * 
	 * @param stat
	 * @return double
	 */
	public double goalsDiffSig (PairStats stat) {
		double x = stat.getTeam2Goals()-stat.getTeam1Goals();
		return 1.0 / (1 + Math.exp(-x));
	}	
	
	
	/**
	 * Calculate the weight of the team1-team2 link based on team1 loss/win ratio
	 * 
	 * @param stat
	 * @return double
	 */
	public double lossWinRatio (int team, PairStats stat) {
		return stat.getTeam2Wins() / (double) stat.getTeam1Wins();			
	}
	
	
	/**
	 * Calculate the weight of the team1-team2 link based on team1 
	 * goals deceded ratio
	 * 
	 * @param stat
	 * @return double
	 */
	public double goalsDecededRatio (PairStats stat) {
		return stat.getTeam2Goals() / (double) (stat.getTeam1Goals() + stat.getTeam2Goals() + 1e-15);		
	}
	
	
	public double lossWinRatioSig (PairStats stat) {
		double l = lossRatio(stat);
		double w = winRatio(stat);
		double x = l-w;
		
		return 1.0 / (1 + Math.exp(-x));
	}
}
