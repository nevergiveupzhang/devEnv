package cn.cnnic.test.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
//		InputStream is = App.class.getClassLoader().getResourceAsStream("test.txt");
//		InputStreamReader isr = new InputStreamReader(is);// 读取
//		// 创建字符流缓冲区
//		BufferedReader bufr = new BufferedReader(isr);
		
		File file=new File(App.class.getResource("/").getFile()+"test.txt");
		BufferedReader bufr = new BufferedReader(new FileReader(file));
		
		String line;
		while ((line = bufr.readLine()) != null) {
			System.out.println(line);
		}
		
		
//		System.out.println(App.class.getResourceAsStream("/"));
//    	FileUtil.init(App.class.getClassLoader().getResource("/").getFile()+"test.txt");
//    	try {
//			FileUtil.writeFile("testtest");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	FileUtil.commit();
	}
}
