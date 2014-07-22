package visualization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Quality;
import org.graphstream.stream.file.FileSinkImages.RendererType;
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
		// Build the graph
		MatchUpGraph g = new MatchUpGraph();                          
		try {
			g.readDataIn();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		g.buildGraph(LinkWeighter.LOSS_RATIO); 
		
		// calculate pagerank, and sort the teams with respect to their ranks
		Matrix ranks = PageRanker.pagerank(g.getAdj(), 0.2);          
		ArrayList<PagerankPair> pRank = new ArrayList<PagerankPair>();
		for (int i = 0; i < ranks.getRowDimension(); i++)
			pRank.add(new PagerankPair(i, ranks.get(i, 0)));
		Collections.sort(pRank);
		
		// calculate the size of each node, which depends on it's pagerank
		int [] nodeSize = new int [pRank.size()];
		nodeSize[pRank.get(0).id] = 13;
		for (int i = 1; i < ranks.getRowDimension(); i++) 
			nodeSize[pRank.get(i).id] = (int) Math.round(nodeSize[pRank.get(i-1).id] * 
					(pRank.get(i).rank / pRank.get(i-1).rank));
		
		// add nodes to the graph	
		Matrix A = g.getAdj();		
		Graph graph = new SingleGraph("football");
		for (int i = 0; i < A.getRowDimension(); i++) { 
			graph.addNode(g.id[i] + "");
			graph.getNode(g.id[i] + "").setAttribute(
					"ui.style", "size: " + nodeSize[i] + "px; fill-image: url('/home/skyra/workspace/football_ranks/files/flags-svg/" + g.id[i] + ".png');");
		}
			
		// remove some links, and add the rest to the graph
		A = leaveTopLinks(A, 5);
		int from, to;
		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				if (A.get(i, j) <= 0)
					continue;
				from = g.id[i];
				to = g.id[j];
				graph.addEdge(from + "," + to, "" + from, "" + to, true);
			}
		}
		
		// set some atributes about the graphics
		graph.addAttribute("ui.antialias", true);
		graph.addAttribute("ui.quality", true);
		graph.addAttribute("ui.stylesheet", "url('files/css/graph-style.css')");
		
		// set the sink
		FileSinkImages pic = new FileSinkImages(OutputType.PNG, Resolutions.QSXGA);
		pic.setStyleSheet("url('files/css/graph-style.css')");
		pic.setLayoutPolicy(LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
		pic.setQuality(Quality.HIGH);
		pic.setRenderer(RendererType.SCALA);
		
		// set the renderer
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		// create an image
		try {
			pic.writeAll(graph, "files/images/graph.png");			
		} catch (IOException e) {
			e.printStackTrace();
		}
					
		// display graph
		graph.display().enableAutoLayout();	
		
		// take screenshots every 2 sec.
		while (true) {
			graph.addAttribute("ui.screenshot", "files/images/graph-screen.bmp");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Remove all but the top N links
	 * 
	 * @param m
	 * @param topN
	 * @return
	 */
	public static Matrix leaveTopLinks (Matrix m, int topN) {
		Matrix tmp;
		double [] elements;
		double min;
		for (int i = 0; i < m.getRowDimension(); i++) {
			tmp = m.getMatrix(i, i, 0, m.getColumnDimension()-1);
			elements = tmp.getRowPackedCopy();
			Arrays.sort(elements);
			min = elements[elements.length-topN];
			
			for (int j = 0; j < m.getColumnDimension(); j++) {
				if (m.get(i, j) < min)
					m.set(i, j, 0);
			}					
		}
		
		return m;
	}
	
}
