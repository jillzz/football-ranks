package ranking;

import java.sql.SQLException;

public class Main {

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
		graph.buildGraph(LinkWeighter.LOSS_RATIO);

	}

}
