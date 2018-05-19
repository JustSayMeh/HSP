package model;

import android.util.Log;

public class Cursor {
	private int Index = 0;
	private int Position = 0;
	private Wrapper[] mass;
	private boolean req = false;

	public Cursor(Wrapper[] m) {
		mass = m;
	}

	public void add(char h) {
		add(h, Index, Position);
	}

	private void add(char h, int index, int pos) {

		if (index > 3 || index < 0)
			return;

		char old = h;
		int localPosition = pos;
		if (h == '0'
				&& (localPosition == 0 || (localPosition == 1 && mass[index].m[0] == '0')))
			mass[index].zeroCount++;
		do {
			h = old;
			old = mass[index].m[localPosition];

			if (old == '0'
					&& (localPosition == 0 || (localPosition == 1 && mass[index].m[0] == '0'))) {
				if (localPosition == 0 && mass[index].m[1] == '0') {
					mass[index].m[0] = '0';
					mass[index].m[1] = h;
				} else {
					mass[index].m[0] = h;
				}
				mass[index].zeroCount--;
				return;
			}

			mass[index].m[localPosition++] = h;
		} while (old != Character.UNASSIGNED && localPosition < 3);

		if (++mass[index].size == 4)
			transactionRight(index, old);
	}



	private void transactionRight(int index, char o) {
		mass[index].size--;
		if (o == Character.UNASSIGNED)
			return;
		if (index++ == 3)
			return;

		add(o, index, 0);
	}

	public void remove() {
		int i = 4;
		while (mass[--i].size == 0)
			;
		Log.i("Photo", i + "i");
		Wrapper th = mass[i];
		th.m[--th.size] = Character.UNASSIGNED;
		if (th.size == th.zeroCount) {
			th.zeroCount = 0;
			th.m = new char[3];
			th.size = 0;
			th.hideZerpCount = 0;
		}
	}

	public void calculiteIndex(int s, int c) {
		int count = 0;
		for (int i = 0; i <= c; i++) {
			count += mass[i].hideZerpCount;
		}
		s -= c - count;

		Index = s / 3;
		Position = s % 3;

	}

	public int getPosition() {
		return Index + Position;
	}
	
	public int getCount(){
		return mass[0].size;
	}

}
