package bunny.io;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.zip.*;

public class ZipControl {
	public static final int BUFFER = 2048;
	public static void main(String[] args) {
		zipSrc();
	}
	public static void zipSrc() {
		zip("./src", "src.zip");
	}
	public static void zip(String dir, String outDir) {
		try {
			BufferedInputStream origin = null;
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outDir)));

			byte data[] = new byte[BUFFER];

			ArrayList<Path> paths = new ArrayList<>();
			findAllSubPaths(Paths.get(dir), paths); 

			for (Path path:paths) {
				origin = new BufferedInputStream(new FileInputStream(path.toString()), BUFFER);
				out.putNextEntry(new ZipEntry(path.toString()));
				int count;
				System.out.println("compressing: " + path.toString());
				while((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void findAllSubPaths(Path p, ArrayList<Path> saveTo) {
		try {
			DirectoryStream<Path> paths = Files.newDirectoryStream(p);
			for (Path path:paths) {
				if (Files.isDirectory(path)) {
					findAllSubPaths(path, saveTo);
				} else {
					saveTo.add(path);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
