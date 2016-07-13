package bunny.code;

import java.io.IOException;

import bunny.data.Reader;
import bunny.data.Writer;
import bunny.data.ZipControl;

public class CodeTemplate {

	static int n;
	
	public static void main(String[] args) {
		try {
			Reader r = new Reader("data.in");
			//Reader r = new Reader(); // for standard input
			
			Writer w = new Writer("answer.txt");
			//Writer w = new Writer(); // for standard output
			
			Problem.init();

			n = r.readInt();
			for (int c = 0; c < n; c++) {
				Problem p = new Problem();
				
				// Input
				int t = r.readInt();
				p.data = new int[t];
				for (int i = 0; i < t; i++) {
					p.data[i] = r.readInt();
				}
				
				
				p.solve();
				
				
				// Output
				w.println(p.result);
			}
			
			r.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ZipControl.zipSrc();
	}
	
	
	public static class Problem {
		
		public int[] data;
		public int result;
		
		public static void init() {
			
		}
		
		public void solve() {
			
		}
	}

}
