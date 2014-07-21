package visualization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Quality;
import org.graphstream.stream.file.FileSinkImages.RendererType;
import org.graphstream.stream.file.FileSinkImages.Resolution;
import org.graphstream.stream.file.FileSinkImages.Resolutions;

import ranking.LinkWeighter;
import ranking.MatchUpGraph;
import ranking.PageRanker;
import ranking.PagerankPair;
import weka.core.matrix.Matrix;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MatchUpGraph g = new MatchUpGraph();                          // new graph object
		try {
			g.readDataIn();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		g.buildGraph(LinkWeighter.LOSS_RATIO);                        // build the graph
		Matrix ranks = PageRanker.pagerank(g.getAdj(), 0.2);          // calculate pagerank
		
		ArrayList<PagerankPair> pRank = new ArrayList<PagerankPair>();
		for (int i = 0; i < ranks.getRowDimension(); i++)
			pRank.add(new PagerankPair(i, ranks.get(i, 0)));
		Collections.sort(pRank);
		
		
		int [] nodeSize = new int [pRank.size()];
		nodeSize[pRank.get(0).id] = 18;
		for (int i = 1; i < ranks.getRowDimension(); i++) 
			nodeSize[pRank.get(i).id] = (int) Math.round(nodeSize[pRank.get(i-1).id] * 
					(pRank.get(i).rank / pRank.get(i-1).rank));
		
			
		Matrix A = g.getAdj();		
		Graph graph = new SingleGraph("football-matches", true, true);  // graph with only one edge between vertices
		for (int i = 0; i < A.getRowDimension(); i++) { 
			graph.addNode(g.id[i] + "");
			graph.getNode(g.id[i] + "").setAttribute(
					"ui.style", "size: " + nodeSize[i] + "px; fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + g.id[i] + ".gif');");
			//graph.getNode(g.id[i] + "").setAttribute("ui.style", 
				//	"fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + g.id[i] + ".gif');");			
		}
		graph.getNode(372 + "").setAttribute("ui.style", 
				"fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + 372 + ".png');");
		graph.getNode(263 + "").setAttribute("ui.style", 
				"fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + 263 + ".png');");
		graph.getNode(345 + "").setAttribute("ui.style", 
				"fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + 345 + ".png');");
		graph.getNode(307 + "").setAttribute("ui.style", 
				"fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + 307 + ".png');");
		graph.getNode(383 + "").setAttribute("ui.style", 
				"fill-image: url('/home/skyra/workspace/football_ranks/files/flags/" + 383 + ".png');");
	
		graph.setAttribute("layout.stabilization-limit", 0.1);
			
		int from, to;
		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				if (A.get(i, j) == 0)
					continue;
				from = g.id[i];
				to = g.id[j];
				graph.addEdge(from + "," + to, "" + from, "" + to, true);
			}
		}
		
		graph.addAttribute("ui.antialias", true);
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.stylesheet", "url('files/css/graph-style.css')");
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		FileSinkImages pic = new FileSinkImages(OutputType.PNG,
				new FileSinkImages.CustomResolution(3000, 3000));
		pic.setLayoutPolicy(LayoutPolicy.COMPUTED_IN_LAYOUT_RUNNER);
		pic.setQuality(Quality.HIGH);
		pic.setAutofit(true);
		pic.setRenderer(RendererType.SCALA);
		pic.setLayoutStabilizationLimit(0.1);
		pic.stabilizeLayout(0.1);
		
		try {
			pic.writeAll(graph, "files/images/graph.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		graph.display();
	}

}
