package sepm.creche.utils;

import java.security.SecureRandom;

/**
 * Generates random Code with given size @param <codeLength>.
 * 
 * @author ASPIR
 *
 */
public class CodeGenerator
{
	public static final int codeLength = 6;

	public String generateCode()
	{
		char[] charPool = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		char c;
		StringBuilder sb = new StringBuilder();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < codeLength; i++)
		{
			c = charPool[sr.nextInt(charPool.length)];
			sb.append(c);
		}

		String output = sb.toString();
		return output;
	}
}