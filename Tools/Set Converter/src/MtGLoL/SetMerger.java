package MtGLoL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class SetMerger {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TAIL = "version control:\r\n	type: none\r\napprentice code: ";

	public static void main(String... args) throws IOException {
		File master = new File("base.mse-set");
		File source = new File("Card Sets");
		
		SetMerger.merge(master, source);
	}
	
	public static void merge(File target, File source) throws IOException {
		Collection<File> sourceSets = FileUtils.listFiles(source, new RegexFileFilter("^(.*mse-set)"),
				DirectoryFileFilter.DIRECTORY);
		merge(target, sourceSets);
		System.out.println("Sets merged into " + target);
		for (File file : sourceSets) {
			System.out.println(file.getAbsolutePath());
		}
	}

	public static void merge(File target, Collection<File> sourceSets) throws IOException {
		StringBuilder sb = new StringBuilder();
		String master = load(target);
		sb.append(master);
		for (File source : sourceSets) {
			String[] split = load(source).split("card:|keyword:");
			for (int i = 0; i < split.length; i++) {
				String block = split[i];
				if (block.trim().contains("has styling:")) {
					sb.append("card:" + block);
				} else if (block.trim().contains("keyword:")) {
					sb.append("keyword:" + block);
				}
			}
		}
		sb.append(TAIL);
		master = sb.toString();
		save(master);
	}

	public static String load(File file) throws IOException {
		ZipFile zip = new ZipFile(file);
		ZipEntry entry = zip.getEntry("set");
		InputStream inputStream = zip.getInputStream(entry);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(LINE_SEPARATOR);
			}
		}
		zip.close();
		return sb.toString();
	}

	public static void save(String master) throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		Path path = Paths.get("output.mse-set");
		URI uri = URI.create("jar:" + path.toUri());
		try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
			Path nf = fs.getPath("set");
			try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
				writer.write(master);
			}
		}

	}
}