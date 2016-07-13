package bunny.util;

public class Base {
	public static int getDigitLength(long number) {
		return getDigitLength(number, 10);
	}
	public static int getDigitLength(long number, int base) {
		if (number == 0) return 1;
		int count = 0;
		while (number != 0) {
			count++;
			number /= base;
		}
		return count;
	}
	
	public static int[] getDigits(long number) {
		return getDigits(number, 10);
	}
	public static int[] getDigits(long number, int base) {
		int l = getDigitLength(number, base);
		int[] digits = new int[l];
		for (int i = l - 1; i >= 0; i--) {
			digits[i] = (int)(number % base);
			number /= base;
		}
		return digits;
	}
	
	public static long fromDigits(int[] digits) {
		return fromDigits(digits, 10);
	}
	public static long fromDigits(int[] digits, int base) {
		long number = 0;
		for (int i = 0; i < digits.length; i++) {
			number *= base;
			number += digits[i];
		}
		return number;
	}
	
	public static String toString(long number, int base) {
		return digitsToString(getDigits(number, base));
	}
	public static String digitsToString(int[] digits) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < digits.length; i++) {
			if (digits[i] < 0 || digits[i] >= 36) {
				str.append('?');
			} else if (digits[i] < 10) {
				str.append((char)('0' + digits[i]));
			} else {
				str.append((char)('A' + digits[i] - 10));
			}
		}
		return str.toString();
	}
	
	public static long fromString(String number, int base) {
		return fromDigits(stringToDigits(number), base);
	}
	public static int[] stringToDigits(String number) {
		int[] digits = new int[number.length()];
		for (int i = 0; i < digits.length; i++) {
			char c = number.charAt(i);
			if (c >= '0' && c <= '9') {
				digits[i] = c - '0';
			} else if (c >= 'a' && c <= 'z') {
				digits[i] = c - 'a' + 10;
			} else if (c >= 'A' && c <= 'Z') {
				digits[i] = c - 'A' + 10;
			} else {
				return null;
			}
		}
		return digits;
	}
	
}
