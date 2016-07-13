package bunny.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import bunny.wrap.ListView;

public class SuperFor extends SimpleIterable<int[]> {

	private int[] current;
	private int[] realEds;
	private ArrayList<Integer> eds;
	private ArrayList<Function<List<Integer>, Integer>> edFuncs;
	private ArrayList<Integer> sts;
	private ArrayList<Function<List<Integer>, Integer>> stFuncs;
	private ArrayList<Integer> steps;
	
	public static SuperFor create() {
		return new SuperFor();
	}
	public static SuperFor range(int... n) {
		SuperFor result = new SuperFor();
		for (int i = 0; i < n.length; i++) {
			result.addForBlock(n[i]);
		}
		return result;
	}
	public static SuperFor range(int[] start, int[] end) {
		SuperFor result = new SuperFor();
		for (int i = 0; i < start.length; i++) {
			result.addForBlock(start[i], end[i]);
		}
		return result;
	}
	public static SuperFor range(int[] start, int[] end, int[] steps) {
		SuperFor result = new SuperFor();
		for (int i = 0; i < start.length; i++) {
			result.addForBlock(start[i], end[i], steps[i]);
		}
		return result;
	}
	public static SuperFor smallerThanPrevious(int n, int layer) {
		SuperFor result = new SuperFor();
		result.addForBlock(n);
		for (int i = 1; i < layer; i++) {
			result.addForBlockUsingPreviousParameter(0, 0, 1, 1);
		}
		return result;
	}
	public static SuperFor strictlySmallerThanPrevious(int n, int layer) {
		SuperFor result = new SuperFor();
		result.addForBlock(n);
		for (int i = 1; i < layer; i++) {
			result.addForBlockUsingPreviousParameter(0, 0, 0, 1);
		}
		return result;
	}
	public static SuperFor greaterThanPrevious(int n, int layer) {
		SuperFor result = new SuperFor();
		result.addForBlock(n);
		for (int i = 1; i < layer; i++) {
			result.addForBlockUsingPreviousParameter(0, 1, n, 0);
		}
		return result;
	}
	public static SuperFor strictlyGreaterThanPrevious(int n, int layer) {
		SuperFor result = new SuperFor();
		result.addForBlock(n);
		for (int i = 1; i < layer; i++) {
			result.addForBlockUsingPreviousParameter(1, 1, n, 0);
		}
		return result;
	}
	public static SuperFor fromMultidimensionalArray(Object array) {
		SuperFor result = new SuperFor();
		Class<?> c = array.getClass();
		int dim = 0;
		while (c.isArray()) {
			dim++;
			c = c.getComponentType();
		}
		for (int i = 0; i < dim; i++) {
			result.addForBlockUsingPreviousParameters(new MultidimensionalArrayRangeFunc(array, i));
		}
		return result;
	}
	public static SuperFor fromMultidimensionalList(Object list) {
		SuperFor result = new SuperFor();
		Object l = list;
		int dim = 0;
		while (l instanceof List<?>) {
			dim++;
			l = ((List<?>)l).get(0);
		}
		for (int i = 0; i < dim; i++) {
			result.addForBlockUsingPreviousParameters(new MultidimensionalListRangeFunc(list, i));
		}
		return result;
	}
	
	
	private SuperFor() {
		eds = new ArrayList<Integer>();
		edFuncs = new ArrayList<Function<List<Integer>, Integer>>();
		sts = new ArrayList<Integer>();
		stFuncs = new ArrayList<Function<List<Integer>, Integer>>();
		steps = new ArrayList<Integer>();
	}
	
	public SuperFor addForBlock(int range) {
		return addForBlock(0, range, 1);
	}
	public SuperFor addForBlock(int st, int ed) {
		return addForBlock(st, ed, 1);
	}
	public SuperFor addForBlock(int st, int ed, int step) {
		eds.add(ed);
		edFuncs.add(null);
		sts.add(st);
		stFuncs.add(null);
		steps.add(step);
		return this;
	}
	public SuperFor addForBlockUsingPreviousParameter(int rangeDelta, int prevParamMultiplierToRange) {
		return addForBlockUsingPreviousParameter(0, 0, rangeDelta, prevParamMultiplierToRange, 1);
	}
	public SuperFor addForBlockUsingPreviousParameter(int stDelta, int prevParamMultiplierToSt, int edDelta, int prevParamMultiplierToEd) {
		return addForBlockUsingPreviousParameter(stDelta, prevParamMultiplierToSt, edDelta, prevParamMultiplierToEd, 1);
	}
	public SuperFor addForBlockUsingPreviousParameter(int stDelta, int prevParamMultiplierToSt, int edDelta, int prevParamMultiplierToEd, int step) {
		eds.add(0);
		edFuncs.add(new PrevParamMultiplierFunc(prevParamMultiplierToEd, edDelta));
		sts.add(0);
		stFuncs.add(new PrevParamMultiplierFunc(prevParamMultiplierToSt, stDelta));
		steps.add(step);
		return this;
	}
	public SuperFor addForBlockUsingPreviousParameters(Function<List<Integer>, Integer> rangeFunc) {
		return addForBlockUsingPreviousParameters(null, rangeFunc, 1);
	}
	public SuperFor addForBlockUsingPreviousParameters(Function<List<Integer>, Integer> stFunc, Function<List<Integer>, Integer> edFunc) {
		return addForBlockUsingPreviousParameters(stFunc, edFunc, 1);
	}
	public SuperFor addForBlockUsingPreviousParameters(Function<List<Integer>, Integer> stFunc, Function<List<Integer>, Integer> edFunc, int step) {
		eds.add(0);
		edFuncs.add(edFunc);
		sts.add(0);
		stFuncs.add(stFunc);
		steps.add(step);
		return this;
	}
	public SuperFor removeForBlock() {
		return removeForBlock(eds.size() - 1);
	}
	public SuperFor removeForBlock(int index) {
		eds.remove(index);
		edFuncs.remove(index);
		sts.remove(index);
		stFuncs.remove(index);
		steps.remove(index);
		return this;
	}
	

	@Override
	protected int[] nextValue(int[] last) {
		int pos;
		if (eds.size() == 0) return null;
		if (last == null) {
			current = new int[eds.size()];
			realEds = new int[eds.size()];
			pos = 0;
			current[pos] = stFuncs.get(pos) == null ? sts.get(pos) : stFuncs.get(pos).apply(new ArrayList<Integer>(0));
			realEds[pos] = edFuncs.get(pos) == null ? eds.get(pos) : edFuncs.get(pos).apply(new ArrayList<Integer>(0));
		} else {
			pos = current.length - 1;
			current[pos] += steps.get(pos);
		}
		
		boolean notInRange;
		while ((notInRange = !inRange(current[pos], realEds[pos], steps.get(pos))) || pos < current.length - 1) {
			if (notInRange) {
				pos--;
				if (pos < 0) return null;
				current[pos] += steps.get(pos);
			} else {
				pos++;
				current[pos] = stFuncs.get(pos) == null ? sts.get(pos) : stFuncs.get(pos).apply(ListView.of(current).range(0, pos));
				realEds[pos] = edFuncs.get(pos) == null ? eds.get(pos) : edFuncs.get(pos).apply(ListView.of(current).range(0, pos));
			}
		}
		return current;
	}
	
	private boolean inRange(int i, int ed, int step) {
		return step > 0 ? i < ed : i > ed;
	}

	public static class PrevParamMultiplierFunc implements Function<List<Integer>, Integer> {
		private int multiplier;
		private int delta;
		public PrevParamMultiplierFunc(int multiplier, int delta) {
			this.multiplier = multiplier;
			this.delta = delta;
		}
		@Override
		public Integer apply(List<Integer> t) {
			return t.size() == 0 ? delta : multiplier * t.get(t.size() - 1) + delta;
		}
	};
	
	public static class MultidimensionalArrayRangeFunc implements Function<List<Integer>, Integer> {
		private Object array;
		private int layer;
		public MultidimensionalArrayRangeFunc(Object array, int layer) {
			this.array = array;
			this.layer = layer;
		}
		@Override
		public Integer apply(List<Integer> t) {
			int i = layer;
			Object a = array;
			while (i > 0) {
				a = Array.get(a, t.get(t.size() - i));
				if (a == null) return 0;
				i--;
			}
			return Array.getLength(a);
		}
	};
	
	public static class MultidimensionalListRangeFunc implements Function<List<Integer>, Integer> {
		private Object list;
		private int layer;
		public MultidimensionalListRangeFunc(Object list, int layer) {
			this.list = list;
			this.layer = layer;
		}
		@Override
		public Integer apply(List<Integer> t) {
			int i = layer;
			Object l = list;
			while (i > 0) {
				l = ((List<?>)l).get(t.get(t.size() - i));
				if (l == null) return 0;
				i--;
			}
			return ((List<?>)l).size();
		}
	};
}
