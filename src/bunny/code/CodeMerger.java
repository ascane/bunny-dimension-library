package bunny.code;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bunny.data.Reader;
import bunny.data.Writer;

// Incomplete!
public class CodeMerger {

	private static enum LineType {
		PACKAGE,
		JAVA_IMPORT,
		BUNNY_IMPORT,
		CLASS,
		OTHER
	}
	private static Pattern importContent = Pattern.compile("(\\S+)\\s*;\\s*");
	
	public static void main(String[] args) {
		merge("src/MergeTest.java");
	}
	
	public static void merge(String mainJavaFilePath) {
		Path path = Paths.get(mainJavaFilePath);
		String outputName = path.getFileName().toString().split("\\.")[0] + ".txt";
		StringBuilder content = new StringBuilder();
		Set<String> javaImports = new HashSet<String>();
		List<String> bunnyImports = new ArrayList<String>();
		Set<String> bunnyImportsSet = new HashSet<String>();
		int index = 0;
		
		addFile(mainJavaFilePath, content, javaImports, bunnyImports, bunnyImportsSet, true);
		while (index < bunnyImports.size()) {
			addFile(bunnyPath(bunnyImports.get(index)), content, javaImports, bunnyImports, bunnyImportsSet, false);
			index++;
		}
		ArrayList<String> imports = new ArrayList<String>(javaImports);
		Collections.sort(imports);
		
		try {
			Writer w = new Writer(outputName);
			
			for (String str : imports) {
				w.println(str);
			}
			w.println();
			w.print(content);
			
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addFile(String javaFilePath, StringBuilder content, Set<String> javaImports, List<String> bunnyImports, Set<String> bunnyImportsSet, boolean isMain) {
		try {
			System.out.println("Merging source file: " + javaFilePath);
			Reader r = new Reader(javaFilePath);
			String line;
			while (true) {
				line = r.readLine();
				LineType t = lineType(line);
				if (t == LineType.JAVA_IMPORT) {
					javaImports.add(line);
				} else if (t == LineType.BUNNY_IMPORT) {
					if (bunnyImportsSet.add(line)) {
						bunnyImports.add(line);
					}
				} else if (t == LineType.CLASS) {
					break;
				}
			}
			content.append("\n");
			if (!isMain) {
				line = line.replace("public ", "");
			}
			content.append(line + "\n");
			while (r.hasNext()) {
				String l = r.readLine();
				if (!isCallingCodeMerger(l)) {
					content.append(l + "\n");
				}
			}
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static LineType lineType(String line) {
		if (line.startsWith("package ")) {
			return LineType.PACKAGE;
		}
		if (line.startsWith("import ")) {
			if (line.startsWith("import bunny.")) {
				return line.contains("CodeMerger") ? LineType.OTHER : LineType.BUNNY_IMPORT;
			} else {
				return LineType.JAVA_IMPORT;
			}
		}
		if (line.startsWith("public class ") || line.startsWith("public abstract class ") || line.startsWith("public interface ")) {
			return LineType.CLASS;
		}
		return LineType.OTHER;
	}
	
	private static String bunnyPath(String line) {
		Matcher m = importContent.matcher(line);
		m.find();
		return "src/" + m.group(1).replace(".", "/") + ".java";
	}
	
	private static boolean isCallingCodeMerger(String line) {
		return line.contains("CodeMerger.");
	}
}
