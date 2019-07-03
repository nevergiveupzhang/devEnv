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

	 public static boolean deleteFile(String fileName){
	        File file = new File(fileName);
	        if(file.isFile() && file.exists()){
	            Boolean succeedDelete = file.delete();
	            if(succeedDelete){
	                return true;
	            }
	            else{
	                return true;
	            }
	        }else{
	            return false;
	        }
	    }


	    public static boolean deleteDirectory(String dir){
	        //如果dir不以文件分隔符结尾，自动添加文件分隔符
	        if(!dir.endsWith(File.separator)){
	            dir = dir+File.separator;
	        }
	        File dirFile = new File(dir);
	        //如果dir对应的文件不存在，或者不是一个目录，则退出
	        if(!dirFile.exists() || !dirFile.isDirectory()){
	            return false;
	        }
	        boolean flag = true;
	        //删除文件夹下的所有文件(包括子目录)
	        File[] files = dirFile.listFiles();
	        for(int i=0;i<files.length;i++){
	            //删除子文件
	            if(files[i].isFile()){
	                flag = deleteFile(files[i].getAbsolutePath());
	                if(!flag){
	                    break;
	                }
	            }
	            //删除子目录
	            else{
	                flag = deleteDirectory(files[i].getAbsolutePath());
	                if(!flag){
	                    break;
	                }
	            }
	        }

	        if(!flag){
	            return false;
	        }

	        //删除当前目录
	        if(dirFile.delete()){
	            return true;
	        }else{
	            return false;
	        }
	    }
}
