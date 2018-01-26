package cn.huhuiyu.database.codebuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多目录代码生成工具
 * 
 * @author 胡辉煜
 * 
 */
public class MutilCoderUtil {

	public List<File> bulidFiles() throws Exception {
		CoderUtil util = new CoderUtil();
		util.setCoderInfo(coderInfo);
		util.setPackageName(packageName);
		List<File> outFiles = new ArrayList<File>();
		File[] dirs = new File(dir).listFiles();
		for (File file : dirs) {
			if (file.isDirectory()) {
				util.setDir(file.getAbsolutePath());
				outFiles.addAll(util.bulidFiles());
			}
		}
		return outFiles;
	}

	private String dir; // 配置文件目录
	private String packageName; // 包名称
	private CoderInfo coderInfo;

	public MutilCoderUtil() {
	}

	public MutilCoderUtil(String dir, String packageName, CoderInfo coderInfo) {
		setDir(dir);
		setPackageName(packageName);
		setCoderInfo(coderInfo);
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public CoderInfo getCoderInfo() {
		return coderInfo;
	}

	public void setCoderInfo(CoderInfo coderInfo) {
		this.coderInfo = coderInfo;
	}

}
