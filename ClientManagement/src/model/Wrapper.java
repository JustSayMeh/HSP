package model;

public class Wrapper {
	public int size = 0;
	public char[] m = new char[3];
	public int zeroCount = 0;
	public int hideZerpCount = 0;
	private boolean head = false;

	public Wrapper(boolean h) {
		head = h;
	}

	public Wrapper() {

	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		for (char i : m) {
			str.append(i);
		}
		String res = str.toString();
		if (res.charAt(zeroCount) != Character.UNASSIGNED) {
			res = res.substring(zeroCount, 3);
			hideZerpCount = zeroCount;
		}
		if (!head)
			res = "." + res;

		return res;
	}
}
