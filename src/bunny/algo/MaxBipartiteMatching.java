package bunny.algo;

import java.util.Arrays;

public class MaxBipartiteMatching {
	  static final int FREE = -1;
	  boolean[][] adjacency;
	  int n0, n1;
	  int[] m0, m1;
	  boolean[] djvu;
	  
	  public MaxBipartiteMatching(int n0, int n1, boolean[][] adjacency) {
	    this.n0 = n0;
	    this.n1 = n1;
	    this.adjacency = adjacency;
	    this.m0 = new int[n0];
	    Arrays.fill(this.m0, FREE);
	    this.m1 = new int[n1];
	    Arrays.fill(this.m1, FREE);
	    this.djvu = new boolean[n0];
	  }
	  
	  private boolean dfs(int i) {
	    assert(!djvu[i]);
	    djvu[i] = true;
	    for (int j = 0; j < n1; j++) {
	      if (adjacency[i][j] && (m1[j] == FREE || (!djvu[m1[j]] && dfs(m1[j])))) {
	        m0[i] = j;
	        m1[j] = i;
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  private boolean augmentingPath() {
	    Arrays.fill(djvu, false);
	    for (int i = 0; i < n0; i++) {
	      if (m0[i] == FREE) {
	        if (dfs(i)) {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
	  
	  public int solve() {
	    int val = 0;
	    Arrays.fill(m0, FREE);
	    Arrays.fill(m1, FREE);
	    while (augmentingPath()) {
	      val++;
	    }
	    return val;
	  }
	  
	  public int[] getMatchingTable() {
	    return m0;
	  }
}
