package bunny.algo;

import java.util.Arrays;

// Hungarian algorithm.
// See http://www.cse.ust.hk/~golin/COMP572/Notes/Matching.pdf.
public class MaxWeightedBipartiteMatching {
	static final int FREE = -1;
	static final int NO_PARENT = -1;
	static final double epsilon = 0.000001;
	double[][] weight;
	int n0, n1;
	double[] l0, l1;
	int[] m0, m1; // m0 <= m1
	int[] parent;
	int destination;

	MaxWeightedBipartiteMatching(int n0, int n1, double[][] weight) {
		this.weight = weight;
		this.n0 = n0;
		this.n1 = n1;
		this.l0 = new double[n0];
		this.l1 = new double[n1];
		this.m0 = new int[n0];
		Arrays.fill(this.m0, FREE);
		this.m1 = new int[n1];
		Arrays.fill(this.m1, FREE);
		this.parent = new int[n1];
	}

	// DFS in the equality graph to find an alternating path.
	private boolean dfs(int i) {
		for (int j = 0; j < n1; j++) {
			if (Math.abs(l0[i] + l1[j] - weight[i][j]) < epsilon  && parent[j] == NO_PARENT) {
				parent[j] = i;
				if (m1[j] == FREE) {
					destination = j;
					return true;
				}
				if (dfs(m1[j])) {
					return true;
				}
			}
		}
		return false;
	}

	private void applyAlternatingPath() {
		int jcurrent = destination;
		while(m0[parent[jcurrent]] != FREE) {
			int newjcurrent = m0[parent[jcurrent]];
			m0[parent[jcurrent]] = jcurrent;
			m1[jcurrent] = parent[jcurrent];
			jcurrent = newjcurrent;
		}
		m0[parent[jcurrent]] = jcurrent;
		m1[jcurrent] = parent[jcurrent];
	}

	private void initLabelling() {
		for (int i = 0; i < n0; i++) {
			for (int j = 0; j < n1; j++) {
				l0[i] = Math.max(l0[i], weight[i][j]);
			}
		}
	}

	private void improveLabelling(int start) {
		boolean[] T = new boolean[n1];
		boolean[] S = new boolean[n0];
		S[start] = true;
		for (int j = 0; j < n1; j++) {
			if (parent[j] != NO_PARENT) {
				T[j] = true;
				S[m1[j]] = true;
			}
		}

		double min = Double.MAX_VALUE;
		for (int i = 0; i < n0; i++) {
			for (int j = 0; j < n1; j++) {
				if (S[i] && !T[j]) {
					min = Math.min(min, l0[i] + l1[j] - weight[i][j]);
				}
			}
		}
		for (int i = 0; i < n0; i++) {
			if (S[i]) {
				l0[i] -= min;
			}
		}
		for (int j = 0; j < n1; j++) {
			if (T[j]) {
				l1[j] += min;
			}
		}
	}

	private int getFreeVertex() {
		for (int i = 0; i < n0; i++) {
			if (m0[i] == FREE) {
				return i;
			}
		}
		return -1;
	}

	private double matchingWeight() {
		double val = 0;
		for (int i = 0; i < n0; i++) {
			val += m0[i] == FREE ? 0 : weight[i][m0[i]];
		}
		return val;
	}

	public double solve() {
		initLabelling();
		int start; // Starting index of the alternating path.
		while ((start = getFreeVertex()) != -1) {
			Arrays.fill(parent, NO_PARENT);

			if (!dfs(start)) {
				improveLabelling(start);
				continue;
			}
			applyAlternatingPath();
		}
		return matchingWeight();
	}

	public int[] getMatchingTable() {
		return m0;
	}
}
