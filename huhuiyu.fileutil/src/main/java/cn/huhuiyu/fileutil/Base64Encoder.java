package cn.huhuiyu.fileutil;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 自定义的http编码器，用于加密
 * 
 * @author 胡辉煜
 *
 */
public class Base64Encoder extends FilterOutputStream {

	private static final char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '+', '/' };

	private int charCount;
	private int carryOver;

	public Base64Encoder(OutputStream out) {
		super(out);
	}

	public void write(int b) throws IOException {
		if (b < 0) {
			b += 256;
		}

		if (charCount % 3 == 0) {
			int lookup = b >> 2;
			carryOver = b & 3;
			out.write(chars[lookup]);
		} else if (charCount % 3 == 1) {
			int lookup = ((carryOver << 4) + (b >> 4)) & 63;
			carryOver = b & 15;
			out.write(chars[lookup]);
		} else if (charCount % 3 == 2) {
			int lookup = ((carryOver << 2) + (b >> 6)) & 63;
			out.write(chars[lookup]);
			lookup = b & 63;
			out.write(chars[lookup]);
			carryOver = 0;
		}
		charCount++;

		if (charCount % 57 == 0) {
			out.write('\n');
		}
	}

	public void write(byte[] buf, int off, int len) throws IOException {
		for (int i = 0; i < len; i++) {
			write(buf[off + i]);
		}
	}

	public void close() throws IOException {
		if (charCount % 3 == 1) {
			int lookup = (carryOver << 4) & 63;
			out.write(chars[lookup]);
			out.write('=');
			out.write('=');
		} else if (charCount % 3 == 2) {
			int lookup = (carryOver << 2) & 63;
			out.write(chars[lookup]);
			out.write('=');
		}
		super.close();
	}

	public static String encode(String unencoded) {
		byte[] bytes = null;
		try {
			bytes = unencoded.getBytes("8859_1");
		} catch (UnsupportedEncodingException ignored) {
		}
		return encode(bytes);
	}

	public static String encode(byte[] bytes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(
				(int) (bytes.length * 1.37));
		Base64Encoder encodedOut = new Base64Encoder(out);

		try {
			encodedOut.write(bytes);
			encodedOut.close();

			return out.toString("8859_1");
		} catch (IOException ignored) {
			return null;
		}
	}

}