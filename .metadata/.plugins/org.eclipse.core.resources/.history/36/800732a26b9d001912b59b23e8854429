package cn.cnnic.domainstat.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	private static BufferedWriter bw;
	private static BufferedReader br;
	public final static String READ = "r";
	public final static String WRITE = "w";

	public static void createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}
	public static void createDirectory(String dirName) {
		new File(dirName).mkdirs();
	}
	public static boolean isExists(String filePath) {
		return isExists(new File(filePath));
	}

	public static boolean isExists(File file) {
		return file.exists();
	}

	/*
	 * Repeatable write，manually initialization and flush
	 */
	public static void writeFile(String content) throws IOException {
		if (null == bw) {
			throw new IOException(
					"BufferedWriter instance bw has not been initialized!please call FileUtil.init(filePath) first!");
		}
		bw.write(content);
	}

	/*
	 * Write once，flush automatically
	 */
	public static void writeFile(String filePath, String content) throws IOException {
		if (null == bw) {
			init(filePath,"w");
		}
		bw.write(content);
		commit();
	}

	/*
	 * Initialization by default in write mode
	 */
	public static void init(String filePath) {
		init(filePath, "w");
	}

	public static void init(String filePath, String mode) {
		try {
			if(("r".equals(mode)&&!new File(filePath).exists())||"w".equals(mode)) {
				createFile(filePath);
			}
			
			if ("r".equals(mode)) {
				br = new BufferedReader(new FileReader(filePath));
			} else if ("w".equals(mode)) {
				bw = new BufferedWriter(new FileWriter(filePath, true));
			} else {
				throw new IOException("wrong file mode!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void commit() {
		if (null == bw && null == br) {
			return;
		}
		if (null != bw) {
			try {
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			bw = null;
		}
		if (null != br) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			br = null;
		}

	}

	public static String readLine() throws IOException {
		return (br==null)?null:br.readLine();
	}

	public static void delete(String fileName) {
		File file=new File(fileName);
		if(file.exists()) {
			file.delete();
		}
	}
}
