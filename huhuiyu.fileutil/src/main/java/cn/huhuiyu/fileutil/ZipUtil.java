package cn.huhuiyu.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩解压缩工具类
 * 
 * @author 胡辉煜
 * 
 */
public class ZipUtil {
	private List<String> entitys = new ArrayList<String>();
	private List<String> dirs = new ArrayList<String>();
	private String zipName;
	private int level = 9;

	public ZipUtil() {
	}

	/**
	 * 创建压缩文件
	 * 
	 * @throws Exception
	 */
	public void makeZip() throws Exception {
		entitys.clear();
		FileUtil.makeFileDir(zipName);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipName));
		zos.setLevel(level); // 设定压缩等级
		for (String dir : dirs) {
			// 调用递归方法添加目录中所有文件到zip
			makeZip(new File(dir), "", zos);
		}
		zos.close();
	}

	/**
	 * 递归方法将目录添加到zip（包括目录自己）chang
	 * 
	 * @param dir
	 *            要添加的目录
	 * @param root
	 *            目录在zip中的路径
	 * @param zos
	 *            zip输出流
	 * @throws Exception
	 */
	private void makeZip(File dir, String root, ZipOutputStream zos)
			throws Exception {
		if (!root.endsWith("/") && root.length() > 1) {
			root = root + "/";
		}
		// 创建目录自己到zip
		makeFileZip(dir, root, zos);
		// 输出目录里面的文件
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				// 如果是目录就递归调用
				makeZip(file, root + dir.getName(), zos);
			} else {
				// 如果是文件就直接创建
				makeFileZip(file, root + dir.getName(), zos);
			}
		}
	}

	/**
	 * 添加文件到zip
	 * 
	 * @param file
	 *            要添加的文件
	 * @param root
	 *            要添加的文件在zip中的路径
	 * @param zos
	 *            zip輸出流 chang
	 * @throws Exception
	 */
	private void makeFileZip(File file, String root, ZipOutputStream zos)
			throws Exception {
		// 確保root路徑格式是正確
		if (!root.endsWith("/") && root.length() > 1) {
			root = root + "/";
		}
		String name = file.getName();
		if (file.isDirectory()) {
			// 如果是目录就直接放置路径名称到zip中
			name = root + name + "/";
			if (entitys.contains(name)) {
				return;
			}
			ZipEntry zipEntry = new ZipEntry(name);
			zos.putNextEntry(zipEntry);
			zos.flush();
		} else {
			// 如果是文件就放置路径名称到zip然后输出到zip流
			name = root + name;
			if (entitys.contains(name)) {
				return;
			}
			ZipEntry zipEntry = new ZipEntry(name);
			zos.putNextEntry(zipEntry);
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = new byte[FileUtil.DEFAULT_READ_SIZE];
			int len = fis.read(bytes);
			while (len > 0) {
				zos.write(bytes, 0, len);
				len = fis.read(bytes);
			}
			fis.close();
			zos.flush();
		}
		entitys.add(name);
	}

	/**
	 * 创建zip工具类，指定压缩包文件名
	 * 
	 * @param zipName
	 *            压缩包文件名
	 */
	public ZipUtil(String zipName) {
		this.zipName = zipName;
	}

	/**
	 * 创建zip工具类，指定压缩包文件名和要压缩的文件夹
	 * 
	 * @param zipName
	 *            压缩包文件名
	 * @param dirs
	 *            要要压缩的文件夹列表
	 */
	public ZipUtil(String zipName, String... dirs) {
		this.zipName = zipName;
		this.addDirs(dirs);
	}

	/**
	 * 添加要压缩的目录
	 * 
	 * @param dir
	 *            要压缩的目录
	 */
	public void addDir(String dir) {
		dirs.add(dir);
	}

	/**
	 * 移除要压缩的目录
	 * 
	 * @param dir
	 *            要移除的目录
	 */
	public void removeDir(String dir) {
		dirs.remove(dir);
	}

	/**
	 * 添加要压缩的目录（多个）
	 * 
	 * @param dirs
	 *            要压缩的目录（多个）
	 */
	public void addDirs(String... dirs) {
		for (String dir : dirs) {
			this.dirs.add(dir);
		}
	}

	/**
	 * 清除所有要压缩的目录
	 */
	public void clearDirs() {
		dirs.clear();
	}

	public String getZipName() {
		return zipName;
	}

	/**
	 * 设置输出的zip文件名称
	 * 
	 * @param zipName
	 *            输出的zip文件路径名称
	 */
	public void setZipName(String zipName) {
		this.zipName = zipName;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * 设置输出的zip文件的压缩等级
	 * 
	 * @param level
	 *            输出的zip文件的压缩等级
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 解压zip文件到指定目录
	 * 
	 * @param zipFile
	 *            zip文件
	 * @param outputDir
	 *            输出目录
	 * @throws Exception
	 */
	public static void unzip(String zipFile, String outputDir) throws Exception {
		File output = FileUtil.makeDir(outputDir);
		ZipFile zip = new ZipFile(zipFile);
		// 读取zip中的元素
		Enumeration<? extends ZipEntry> enumeration = zip.entries();
		while (enumeration.hasMoreElements()) {
			ZipEntry e = enumeration.nextElement();
			if (e.isDirectory()) {
				// 如果是目录就直接创建到输出目录
				File dir = new File(output, "/" + e.getName());
				dir.mkdirs();
			} else {
				// 如果是文件就复制到输出目录
				InputStream zis = zip.getInputStream(e);
				FileOutputStream fos = new FileOutputStream(new File(output,
						"/" + e.getName()));
				byte[] bytes = new byte[FileUtil.DEFAULT_READ_SIZE];
				int len = zis.read(bytes);
				while (len > 0) {
					fos.write(bytes, 0, len);
					len = zis.read(bytes);
				}
				zis.close();
				fos.close();
			}
		}
		zip.close();
	}
}
