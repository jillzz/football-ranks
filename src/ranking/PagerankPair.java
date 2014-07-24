package ranking;

/**
 * Id - Pagerank pairs
 *	
 */
public class PagerankPair implements Comparable<PagerankPair> {
	public int id;
	public double rank;
	
	
	public PagerankPair(int id, double rank) {
		this.id = id;
		this.rank = rank;
	}


	@Override
	public int compareTo(PagerankPair o) {
		if (rank > o.rank) return 1;
		if (rank < o.rank) return -1;
		return 0;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		PagerankPair o = (PagerankPair) obj;
		return (id == o.id);
	}
}
