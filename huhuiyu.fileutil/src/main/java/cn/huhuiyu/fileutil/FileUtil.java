package cn.huhuiyu.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import cn.huhuiyu.utils.StringUtils;

/**
 * 自定义静态文件工具集
 * 
 * @author 胡辉煜
 *
 */
public class FileUtil {
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final int DEFAULT_READ_SIZE = 8 * 1024;
	public static final String JAVA_EXT = "java";
	public static final String XML_EXT = "xml";
	public static final String JSP_EXT = "jsp";

	private FileUtil() {
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dir
	 *            文件夹路径名称
	 * @return 创建的文件夹对象
	 * @throws IOException
	 */
	public static File makeDir(String dir) throws IOException {
		File d = new File(dir).getCanonicalFile();
		if (!d.isDirectory()) {
			d.mkdirs();
		}
		return d.getCanonicalFile();
	}

	/**
	 * 确保文件的父目录存在
	 * 
	 * @param file
	 *            文件路径名称
	 * @return 返回文件对象
	 * @throws IOException
	 */
	public static File makeFileDir(String file) throws IOException {
		File f = new File(file).getCanonicalFile();
		File d = f.getParentFile();
		if (!d.isDirectory()) {
			d.mkdirs();
		}
		return f.getCanonicalFile();
	}

	/**
	 * 创建包名称对应的目录
	 * 
	 * @param packageName
	 *            包名称
	 * @param dir
	 *            根目录
	 * @return 创建的包目录
	 * @throws IOException
	 */
	public static File makePackageDir(String packageName, String dir) throws IOException {
		String d = dir + "/";
		d += StringUtils.packageToDir(packageName);
		return makeDir(d);
	}

	/**
	 * 使用nio复制srcFileName文件内容到tagFileName
	 * 
	 * @param srcFileName
	 *            源文件
	 * @param tagFileName
	 *            目标文件
	 * @return 文件复制是否成功复制
	 * @throws IOException
	 *             文件操作发生错误
	 */
	public synchronized static boolean newCopyFile(String srcFileName, String tagFileName) throws IOException {
		File in = new File(srcFileName).getCanonicalFile();
		File out = new File(tagFileName).getCanonicalFile();
		return newCopyFile(in, out);
	}

	/**
	 * 使用nio复制srcFile文件内容到tagFile
	 * 
	 * @param srcFile
	 *            源文件
	 * @param tagFile
	 *            目标文件
	 * @return 文件复制是否成功复制
	 * @throws IOException
	 *             文件操作发生错误
	 */
	public synchronized static boolean newCopyFile(File srcFile, File tagFile) throws IOException {
		// 使用nio复制文件，读取源文件
		FileInputStream inputStream = new FileInputStream(srcFile);
		FileChannel inChannel = inputStream.getChannel();
		long inputSize = inChannel.size();
		MappedByteBuffer inBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputSize);
		// 使用通道方式复制文件
		FileOutputStream outputStream = new FileOutputStream(tagFile);
		FileChannel outChannel = outputStream.getChannel();
		outChannel.write(inBuffer);
		// 关闭相关对象
		inChannel.close();
		inputStream.close();
		outChannel.close();
		outputStream.close();
		return true;
	}

	/**
	 * 复制srcFileName文件内容到tagFileName
	 * 
	 * @param srcFileName
	 *            源文件
	 * @param tagFileName
	 *            目标文件
	 * @return 文件复制是否成功复制
	 * @throws IOException
	 *             文件操作发生错误
	 */
	public synchronized static boolean copyFile(String srcFileName, String tagFileName) throws IOException {
		FileInputStream inputStream = new FileInputStream(srcFileName);
		FileOutputStream outputStream = new FileOutputStream(tagFileName);
		byte[] readBytes = new byte[DEFAULT_READ_SIZE];
		int readLength = inputStream.read(readBytes);
		while (readLength != -1)// 读取数据到文件输出流
		{
			outputStream.write(readBytes, 0, readLength);
			outputStream.flush();
			readLength = inputStream.read(readBytes);
		}
		// 关闭相关对象
		inputStream.close();
		inputStream = null;
		outputStream.close();
		outputStream = null;
		return true;
	}

	/**
	 * 复制srcFile文件内容到tagFile
	 * 
	 * @param srcFile
	 *            源文件
	 * @param tagFile
	 *            目标文件
	 * @return 文件复制是否成功复制
	 * @throws IOException
	 *             文件操作发生错误
	 */
	public synchronized static boolean copyFile(File srcFile, File tagFile) throws IOException {
		return copyFile(srcFile.getAbsolutePath(), tagFile.getAbsolutePath());
	}

	/**
	 * 把str写入fileName文件中，编码方式为DEFAULT_ENCODING
	 * 
	 * @param str
	 *            要输出的字符
	 * @param fileName
	 *            要输出的文件
	 * @throws IOException
	 *             文件操作发生错误
	 */
	public static void stringToFile(String str, String fileName) throws IOException {
		stringToFile(str, fileName, DEFAULT_ENCODING);
	}

	/**
	 * 把str写入fileName文件中，编码方式为charset
	 * 
	 * @param str
	 *            要输出的字符
	 * @param fileName
	 *            要输出的文件
	 * @param charset
	 *            输出的编码方式
	 * @throws IOException
	 *             文件操作发生错误
	 */
	public static void stringToFile(String str, String fileName, String charset) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(makeFileDir(fileName)), charset);
		out.write(str);
		out.flush();
		out.close();
	}

	/**
	 * 读取文本文件内容到字符串，文件编码为DEFAULT_ENCODING
	 * 
	 * @param fileName
	 *            文本文件名称
	 * @return 文本文件内容
	 * @throws Exception
	 */
	public static String readTextFile(String fileName) throws Exception {
		return readTextFile(fileName, DEFAULT_ENCODING);
	}

	/**
	 * 读取文本文件内容到字符串
	 * 
	 * @param fileName
	 *            文本文件名称
	 * @param charset
	 *            字符编码
	 * @return 文本文件内容
	 * @throws Exception
	 */
	public static String readTextFile(String fileName, String charset) throws Exception {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), charset);
		char[] chars = new char[DEFAULT_READ_SIZE];
		int length = -1;
		StringBuffer sb = new StringBuffer();
		while ((length = reader.read(chars)) > -1) {
			sb.append(new String(chars, 0, length));
		}
		reader.close();
		return sb.toString();
	}

	/**
	 * 删除一个目录以及其中的所有文件
	 * 
	 * @param dir
	 *            要删除的目录
	 * @return 删除目录是否成功
	 * @throws IOException
	 */
	public static void deleteDir(File dir) throws IOException {
		dir = dir.getCanonicalFile();
		if (!dir.isDirectory()) {
			return;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else {
				deleteDir(file);
			}
		}
		dir.delete();// 删除目录本身
	}

	/**
	 * 删除一个目录以及其中的所有文件(不处理错误)
	 * 
	 * @param dir
	 *            要删除的目录
	 * @return 删除目录是否成功
	 * @throws IOException
	 */
	public static void deleteDirNoError(File dir) throws IOException {
		dir = dir.getCanonicalFile();
		if (!dir.isDirectory()) {
			return;
		}
		try {
			for (File file : dir.listFiles()) {
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
			dir.delete();// 删除目录本身
		} catch (Exception e) {
		}
	}

	/**
	 * 复制源目录到目标目录
	 * 
	 * @param source
	 *            源目录
	 * @param target
	 *            目标目录
	 * @throws IOException
	 */
	public static void copyDir(File source, File target) throws IOException {
		source = source.getCanonicalFile();
		target = target.getCanonicalFile();
		File dir = new File(target.getCanonicalPath(), source.getName());
		dir.mkdirs();
		File[] files = source.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				File t = new File(dir, file.getName());
				copyFile(file.getCanonicalPath(), t.getCanonicalPath());
			} else if (file.isDirectory()) {
				copyDir(file, dir);
			}
		}
	}

	/**
	 * 复制源目录里面所有文件和目录到目标目录
	 * 
	 * @param source
	 *            源目录
	 * @param target
	 *            目标目录
	 * @throws IOException
	 */
	public static void copyDirInner(File source, File target) throws IOException {
		File[] files = source.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				copyFile(file, new File(target.getCanonicalFile(), file.getName()));
			} else {
				copyDir(file, target);
			}
		}
	}

	/**
	 * 转化包名称为文件路径
	 * 
	 * @param pack
	 *            包名称
	 * @return 包名称对应的文件路径
	 */
	public static String parsePackage(String pack) {
		return pack.replaceAll("[.]", "/");
	}
}
