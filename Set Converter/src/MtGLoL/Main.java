package MtGLoL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;

public class Main {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String MSE_REGEX = "^(.*mse-set)";
	private static final String SET_SYMBOL = "symbol2.mse-symbol";

	private static final String PROPERTIES = "merge.ini";

	public static void main(String... args) throws IOException, BackingStoreException {
		Ini ini = new Ini(new File(PROPERTIES));
		Preferences prefs = new IniPreferences(ini);
		Preferences node = prefs.node("configs");

		String BASE_SET = node.get("BASE_SET", "base.mse-set");
		String SOURCE_DIRECTORY = node.get("SOURCE_DIRECTORY", "Card Sets");
		String OUTPUT_DIRECTORY = node.get("OUTPUT_DIRECTORY", "output");
		String OUTPUT_SET = node.get("OUTPUT_SET", OUTPUT_DIRECTORY + ".mse-set");

		File outputDirectory = new File(OUTPUT_DIRECTORY);
		if (outputDirectory.exists()) {
			FileUtils.cleanDirectory(outputDirectory);
		}
		Collection<File> sources = recursiveFindSources(SOURCE_DIRECTORY);
		ZipFile outputZip = new ZipFile(new File(OUTPUT_SET));

		// Clear output.mse-set
		Set<String> fileNames = outputZip.getFileHeaders().stream().map(header -> header.getFileName())
				.collect(Collectors.toSet());
		for (String fileName : fileNames) {
			outputZip.removeFile(fileName);
		}

		ZipFile baseZip = new ZipFile(new File(BASE_SET));
		baseZip.extractFile(SET_SYMBOL, OUTPUT_DIRECTORY, SET_SYMBOL);
		String baseSetString = read(baseZip.getInputStream(baseZip.getFileHeader("set")));
		StringBuilder master = new StringBuilder(baseSetString);

		int count = 1;
		for (File source : sources) {
			System.out.println("Begin merge of " + source.getAbsolutePath());
			ZipFile sourceZip = new ZipFile(source);
			ZipInputStream setStream = sourceZip.getInputStream(sourceZip.getFileHeader("set"));
			String setString = read(setStream);
			String[] split = setString.split("card:|keyword:");
			for (int i = 0; i < split.length; i++) {
				String block = split[i];
				if (block.trim().contains("has styling:")) {
					String cardName = getField("name:", block);
					System.out.print("\t" + cardName);
					String oldImage = getField("image:", block);
					if (!StringUtils.isEmpty(oldImage)) {
						String newImage = "image" + String.valueOf(count++);
						System.out.println("(" + oldImage + " -> " + newImage + ")");
						block = block.replaceAll("image([0-9]{1,})", newImage);
						sourceZip.extractFile(oldImage, OUTPUT_DIRECTORY, newImage);
					} else {
						System.out.println("");
					}
					master.append("card:" + block);
				} else if (block.trim().contains("keyword:")) {
					master.append("keyword:" + block);
				}
			}
		}

		// Write the set file
		FileWriter writer = new FileWriter(OUTPUT_DIRECTORY + "/set", StandardCharsets.UTF_8);
		writer.write(master.toString());
		writer.close();

		// Zip everything from output directory into output.mse-set
		outputZip.addFiles(new ArrayList<>(recursiveFindAll(OUTPUT_DIRECTORY)));
		outputZip.addFile(OUTPUT_DIRECTORY + "/set");

		System.out.println("Output Raw: " + OUTPUT_DIRECTORY);
		System.out.println("Output Set: " + OUTPUT_SET);
		System.out.println("Done.");
	}

	private static String getField(String field, String block) {
		int index = block.indexOf(field);
		String imageName = block.substring(index + field.length());
		index = imageName.indexOf(LINE_SEPARATOR);
		imageName = imageName.substring(0, index);
		return imageName.trim();
	}

	public static String read(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(LINE_SEPARATOR);
			}
		}
		return sb.toString();
	}

	private static Collection<File> recursiveFindSources(String sourceDirectory) {
		Collection<File> sourceSets = FileUtils.listFiles(new File(sourceDirectory), new RegexFileFilter(MSE_REGEX),
				DirectoryFileFilter.DIRECTORY);
		return sourceSets;
	}

	private static Collection<File> recursiveFindAll(String sourceDirectory) {
		Collection<File> sourceSets = FileUtils.listFiles(new File(sourceDirectory), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		return sourceSets;
	}

}