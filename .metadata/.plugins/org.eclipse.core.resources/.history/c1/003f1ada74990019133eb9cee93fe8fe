package cn.cnnic.domainstat.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	private static BufferedWriter bw;
	
	public static void createFile(String filePath) throws IOException{
		File file = new File(filePath);
		if(isExists(file)) {
			return;
		}
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
	}

	public static boolean isExists(String filePath) {
		File file = new File(filePath);
		return isExists(file);
	}
	public static boolean isExists(File file) {
		return file.exists();
	}
	
	/*
	 * Repeatable write，manually initialization and flush
	 * */
	public static void writeFile(String content) throws IOException {
		if(null==bw) {
			throw new IOException("BufferedWriter instance bw has not been initialized!please call FileUtil.init(filePath) first!");
		}
		bw.write(content);
	}
	
	/*
	 * Write once，flush automatically
	 * */
	public static void writeFile(String filePath, String content) throws IOException {
		if(null==bw) {
			init(filePath);
		}
		bw.write(content);
		commit();
	}

	public static void init(String filePath) {
		try {
			createFile(filePath);
			bw = new BufferedWriter(new FileWriter(filePath,true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void commit() {
		if(null==bw) {
			return;
		}
		try {
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(bw!=null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		bw=null;
	}
}
