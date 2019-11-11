import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class vertexCover {
	public static Bag<Integer> VC;
	
	public static void main (String[] args) throws FileNotFoundException, IOException {
		JOptionPane myWindow;
		myWindow = new JOptionPane();
		String filename;
		filename=myWindow.showInputDialog("Enter External Text File to Open:");
		Scanner scanner = new Scanner (new File (filename));
		JTextArea outputArea = new JTextArea(5,20);
		JScrollPane scroller = new JScrollPane(outputArea);
		outputArea.setText("");
		while (scanner.hasNext()) {
			String fullName = scanner.nextLine();
			outputArea.append(fullName+"\n");
		}
		myWindow.showMessageDialog(null,scroller, "The Original File Was ....", 
				JOptionPane.INFORMATION_MESSAGE);
		
		// Task 2 -- Get input file into a data structure.
		scanner = new Scanner(new File (filename));
		int V=0; // Number of Verticies -- V
		int E=0; // Number of Edges -- E
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			StringTokenizer st = new StringTokenizer(line, "x");
			V += st.countTokens();
		}
		//boolean [][]edges = new boolean[V][V];
		Bag<String> []buffer =(Bag<String>[]) new Bag[V];
		Bag<String> []buffer0 = (Bag<String>[]) new Bag[V];
		for (int i=0;i<V; i++) {
			//buffer[i] = new StringBuilder(V);
			buffer[i] = new Bag<String>();
			buffer0[i] = new Bag<String>();
		}
		scanner = new Scanner(new File (filename));
		for (int i=0; scanner.hasNext();) {
			String line = scanner.nextLine();
			line.replaceAll("\\s","");
			StringTokenizer st = new StringTokenizer(line, "x");
			while (st.hasMoreTokens()) {
				//buffer[i++].append(st.nextToken());
				String nextToken = st.nextToken();
				buffer[i].append(nextToken);
				buffer0[i++].append(nextToken);
			}
		}
		Graph G = new Graph(V);
		for (int i=0; i<V; i++) {
			int size=0;
			//Bag<String>[] copy = buffer.clone();
			for (int q=0; q<buffer[i].size(); q++) {
				while (buffer0[i].size()>0) {
					size += buffer0[i].removeFirst().length();
				}
			}
			StringBuilder line = new StringBuilder(size);
			while (buffer[i].size()>0) {
				line.append(buffer[i].removeFirst());
			}
			//System.out.println(line);
			StringTokenizer st = new StringTokenizer(line.toString(), ":, ");
			int node=0;
			if (st.hasMoreTokens())
				st.nextToken();
			while (st.hasMoreTokens()) {
				int x = Integer.parseInt(st.nextToken());
				//edges[i][x] = true;
				//edges[x][i] = true;
				if (x>=i) { 
					G.addEdge(i,x);
					E++;
				}
			}
		}
		/*// Make a graph out of edges.
		Graph G = new Graph(V);
		for (int i=0; i<V; i++) {
			for (int j=0; j<=i; j++) {
				if (edges[i][j]) {
					G.addEdge(i,j);
				}
			}
		}*/
		
		// G is the Graph
		
		// The algorithm decides if G has a k-node VC
		// Starting at k=0 through k=|V|
		// Search for a k-node VC of G
		VC = new Bag<Integer>(); // The Vertex Cover
		boolean complete = false;
		for (int k=0; complete==false; k++) {
			Graph GG = new Graph(G);
			complete = vertex_cover(GG, k);
		}
		
		// VC is the vertex cover.
		// Window2 - Print out string[]. One per line.
		// Output File code.txt
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		JTextArea outputTextArea = new JTextArea(10,20);
		JScrollPane scroller1 = new JScrollPane(outputTextArea);
		while (VC.isEmpty()==false) {
			int result = VC.removeFirst();
			outputTextArea.append(result+"x");
			writer.print(result+"x");
		}
		myWindow.showMessageDialog(null,scroller1,"Vertex Cover",JOptionPane.INFORMATION_MESSAGE);
		writer.close();
	}

	/**
	 * Checks if graph G has a vertex cover of at most size k.
	 */
	public static boolean vertex_cover (Graph G, int k) {
		if (G.E()==0) return true;
		if (G.E()>k*G.v()) return false;
		// get an edge {u,v} from G
		int u=0,v=0;
		for (;u<G.V(); u++) {
			if (G.adj[u]!=null) {
				if (G.adj[u].isEmpty()==false) {
					v = G.adj[u].first.item;
					break;
				}
			}
		}
		//Recursively check if either G-{u} or G-{v}
		//has a VC of size k-1.
		Graph G0 = new Graph(G);
		Graph G1 = new Graph(G);
		G0.removeVertex(u); 	// G0 is G-{u}
		G1.removeVertex(v);		// G1 is G-{v}
		boolean g1 = vertex_cover(G0, k-1);
		if (g1) {
			VC.add_ifnot_inBag(u);
			return true;
		}
		boolean g2 = vertex_cover(G1, k-1);
		if (g2) {
			VC.add_ifnot_inBag(v);
			return true;
		}
		if (!(g1||g2)) return false;
		return true;
	}
}