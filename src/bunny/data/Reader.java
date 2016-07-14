package bunny.data;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class Reader {
	
	private java.io.Reader inputReader;
	public BufferedReader reader;
	public char[] delimiters = {' ', ','};
	
	public static Reader ofString(String string) {
		return new Reader(new StringReader(string));
	}
	
	public Reader(InputStream input) {
		inputReader = new InputStreamReader(input);
		reader = new BufferedReader(inputReader, 1024);
	}
	public Reader(String path) throws FileNotFoundException {
		inputReader = new FileReader(path);
		reader = new BufferedReader(inputReader, 1024);
	}
	private Reader(StringReader sr) {
		inputReader = sr;
		reader = new BufferedReader(inputReader, 1024);
	}
	
	public char readChar() throws IOException {
		char c;
		do {
			c = (char) reader.read();
		} while (c == '\r' || c == '\n');
		
		if (c == (char) -1) {
			throw new DataDepletedException("No more input left!");
		}
		return c;
	}
	public String readLine() throws IOException {
		String line = reader.readLine();
		if (line == null) {
			throw new DataDepletedException("No more input left!");
		}
		return line;
	}
	public String readWord() throws IOException {
		String word = "";
		char c;
		while (true) {
			c = (char) reader.read();
			if (c == (char) -1) {
				break;
			} else if (isDelimiter(c)) {
				if (word.length() > 0) break;
				else continue;
			} else {
				word += c;
			}
		}
		if (c == '\r') {
			reader.mark(1);
			if ((char) reader.read() != '\n') reader.reset();
		}
		if (word.length() == 0) {
			throw new DataDepletedException("No more input left!");
		}
		return word;
	}
	public String[] readWordArray(int n) throws IOException {
		String[] result = new String[n];
		for (int i = 0; i < n; i++) {
			result[i] = readWord();
		}
		return result;
	}
	public int readInt() throws IOException {
		String word = readWord();
		return Integer.parseInt(word);
	}
	public int[] readIntArray(int n) throws IOException {
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = readInt();
		}
		return result;
	}
	public long readLong() throws IOException {
		String word = readWord();
		return Long.parseLong(word);
	}
	public long[] readLongArray(int n) throws IOException {
		long[] result = new long[n];
		for (int i = 0; i < n; i++) {
			result[i] = readLong();
		}
		return result;
	}
	public float readFloat() throws IOException {
		String word = readWord();
		return Float.parseFloat(word);
	}
	public float[] readFloatArray(int n) throws IOException {
		float[] result = new float[n];
		for (int i = 0; i < n; i++) {
			result[i] = readFloat();
		}
		return result;
	}
	public double readDouble() throws IOException {
		String word = readWord();
		return Double.parseDouble(word);
	}
	public double[] readDoubleArray(int n) throws IOException {
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = readDouble();
		}
		return result;
	}
	public boolean hasNext() throws IOException {
		reader.mark(32);
		try {
			char c;
			do {
				c = readChar();
			} while (isDelimiter(c)); 
		} catch (DataDepletedException e) {
			reader.reset();
			return false;
		}
		reader.reset();
		return true;
	}
	
	public void close() throws IOException {
		reader.close();
		inputReader.close();
	}
	
	private boolean isDelimiter(char c) {
		for (int i = 0; i < delimiters.length; i++) {
			if (c == delimiters[i]) return true;
		}
		if (c == '\n') return true;
		if (c == '\r') return true;
		return false;
	}
	
	public static class DataDepletedException extends IOException {
		private static final long serialVersionUID = 1L;
		public DataDepletedException() {}
		public DataDepletedException(String msg) {
			super(msg);
		}
	}
}
