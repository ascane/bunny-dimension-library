package bunny.code;

import java.io.PrintStream;
import java.util.Scanner;

public class CodeTemplateCompact {

	static int n;
	
	public static void main(String[] args) {
		Scanner r = new Scanner(System.in);
		PrintStream w = System.out;
		
		Problem.init();
		
		n = r.nextInt();
		for (int c = 0; c < n; c++) {
			Problem p = new Problem();
				
			// Input
			int t = r.nextInt();
			p.data = new int[t];
			for (int i = 0; i < t; i++) {
				p.data[i] = r.nextInt();
			}
			
			
			p.solve();
			
			
			// Output
			w.println(p.result);
		}
		
		r.close();
		w.close();
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
