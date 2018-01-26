package test.cn.huhuiyu.fileutil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.huhuiyu.fileutil.NormalCipher;

public class TestFileUtil {

	@Test
	public void testNormalCipher() throws Exception {
		String strTest = "abcd";
		String strTestEncrypt = "UHuoHLtdAbgfRQ9jOvsRmg==";
		String strEncrypt = NormalCipher.getSystemCipher().encryptString(strTest);
		assertEquals(strTestEncrypt, strEncrypt);
		assertEquals(NormalCipher.getSystemCipher().decryptString(strEncrypt), "abcd");
	}

}
