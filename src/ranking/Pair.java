package ranking;

public class Pair {
	private int team1;
	private int team2;
	
	
	public Pair(int team1, int team2) {
		this.team1 = team1;
		this.team2 = team2;
	}

	
	public int opponent (int team) {
		if (team == team1) return team2;
		return team1;
	}
	
	
	public int getTeam1() {
		return team1;
	}


	public void setTeam1(int team1) {
		this.team1 = team1;
	}


	public int getTeam2() {
		return team2;
	}


	public void setTeam2(int team2) {
		this.team2 = team2;		
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		Pair o = (Pair) obj;
		return (team1 == o.team1 && team2 == o.team2);
			   //(team1 == o.team2 && team2 == o.team1); 
	}
	
	
	@Override
	public int hashCode() {
		return 1;
	}
}
