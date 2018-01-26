package cn.huhuiyu.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 一般的加密实现类,通过一个加密文件进行加密
 * 
 * @author 胡辉煜
 */
public class NormalCipher {
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyyMMddhhmmss");
	private static final String SYSTEM_CIPHER_KEY = "huhuiyu.key";
	private static final String CIPHER_NAME = "AES";
	private Key key = null;

	/**
	 * 加密类构造函数
	 * 
	 * @param keyFile
	 *            加密类的密码文件名称
	 * @throws Exception
	 *             无法正常初始化加密文件
	 */
	public NormalCipher(String keyFile) throws Exception {
		this(new FileInputStream(keyFile));
	}

	/**
	 * 加密类构造函数
	 * 
	 * @param keyIn
	 *            加密类的密码文件输入流
	 * @throws Exception
	 *             无法正常初始化加密文件
	 */
	public NormalCipher(InputStream keyIn) throws Exception {
		key = getKey(keyIn);
	}

	/**
	 * 加密文件内容
	 * 
	 * @param inFileName
	 *            被加密的文件
	 * @param outFileName
	 *            加密后的输出文件
	 * @throws Exception
	 *             加密文件内容发生错误
	 */
	public void encrypt(String inFileName, String outFileName) throws Exception {
		InputStream in = new FileInputStream(inFileName);
		OutputStream out = new FileOutputStream(outFileName);
		encrypt(in, out);
	}

	/**
	 * 加密InputStream流的数据，并输出到OutputStream里面，注意：输出完成后会关闭输入输出流。
	 * 
	 * @param in
	 *            需要加密的流
	 * @param out
	 *            加密以后的输出流
	 * @throws Exception
	 */
	public void encrypt(InputStream in, OutputStream out) throws Exception {
		// 初始化加密信息
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 加密文件
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(blockSize);
		byte[] inBytes = new byte[blockSize];
		byte[] outBytes = new byte[outputSize];
		int inLength = 0;
		boolean more = true;
		while (more) {
			inLength = in.read(inBytes);
			if (inLength == blockSize) {
				int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
				out.write(outBytes, 0, outLength);
			} else {
				more = false;
			}
		}
		// 结束输出，关闭相关对象
		if (inLength > 0)
			outBytes = cipher.doFinal(inBytes, 0, inLength);
		else
			outBytes = cipher.doFinal();
		in.close();
		out.write(outBytes);
		out.flush();
		out.close();
	}

	/**
	 * 解密文件内容
	 * 
	 * @param inFileName
	 *            被解密的文件
	 * @param outFileName
	 *            解密后的输出文件
	 * @throws Exception
	 *             解密文件内容发生错误
	 */
	public void decrypt(String inFileName, String outFileName) throws Exception {
		InputStream in = new FileInputStream(inFileName);
		OutputStream out = new FileOutputStream(outFileName);
		decrypt(in, out);
	}

	/**
	 * 解密InputStream流的数据，并输出到OutputStream里面，注意：输出完成后会关闭输入输出流。
	 * 
	 * @param in
	 *            需要解密的流
	 * @param out
	 *            解密后的输出流
	 * @throws Exception
	 */
	public void decrypt(InputStream in, OutputStream out) throws Exception {
		// 初始化加密信息
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 解密文件
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(blockSize);
		byte[] inBytes = new byte[blockSize];
		byte[] outBytes = new byte[outputSize];
		int inLength = 0;
		boolean more = true;
		while (more) {
			inLength = in.read(inBytes);
			if (inLength == blockSize) {
				int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
				out.write(outBytes, 0, outLength);
			} else {
				more = false;
			}
		}
		// 结束输出，关闭相关对象
		if (inLength > 0)
			outBytes = cipher.doFinal(inBytes, 0, inLength);
		else
			outBytes = cipher.doFinal();
		in.close();
		out.write(outBytes);
		out.flush();
		out.close();
	}

	/**
	 * 加密字符串
	 * 
	 * @param text
	 *            被加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 *             加密字符串发生错误
	 */
	public String encryptString(String text) throws Exception {
		byte[] retByte = null;
		byte[] in = text.getBytes(FileUtil.DEFAULT_ENCODING);
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		retByte = cipher.doFinal(in);
		String retStr = Base64Encoder.encode(retByte);
		return retStr;
	}

	/**
	 * 解密字符串
	 * 
	 * @param text
	 *            被解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 *             解密字符串发生错误
	 */
	public String decryptString(String text) throws Exception {
		byte[] retByte = null;
		byte[] sorData = Base64Decoder.decodeToBytes(text);
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.DECRYPT_MODE, key);
		retByte = cipher.doFinal(sorData);
		return new String(retByte, FileUtil.DEFAULT_ENCODING);
	}

	/**
	 * 通过文件加载密钥对象
	 * 
	 * @param keyFileName
	 *            加密类的密码输入流，一般为文件流
	 * @return 密钥对象
	 * @throws Exception
	 *             无法通过文件加载密钥对象
	 */
	private Key getKey(InputStream keyIn) throws Exception {
		ObjectInputStream keyObject = new ObjectInputStream(keyIn);
		Key key = (Key) keyObject.readObject();
		keyIn.close();
		return key;
	}

	/**
	 * 获取系统默认加密对象
	 * 
	 * @return 系统默认加密对象
	 * @throws Exception
	 */
	public static NormalCipher getSystemCipher() throws Exception {
		InputStream is = NormalCipher.class
				.getResourceAsStream(SYSTEM_CIPHER_KEY);
		NormalCipher cipher = new NormalCipher(is);
		return cipher;
	}

	/**
	 * 生成加密类的密码文件，并输出到keyFile指定的文件。
	 * 
	 * @param keyFile
	 *            密码输出文件名称
	 * @return 生成加密类的密码文件对象
	 * @throws Exception
	 *             无法生成加密类的密码文件
	 */
	public static File generateKey(String keyFile) throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance(CIPHER_NAME); // 密码生成器
		SecureRandom random = new SecureRandom(); // 产生随机密码种子
		keygen.init(random);
		SecretKey key = keygen.generateKey(); // 生成密码对象
		File file = FileUtil.makeFileDir(keyFile);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				file));
		out.writeObject(key);
		out.close(); // 输出密码对象到文件
		return file;
	}

}