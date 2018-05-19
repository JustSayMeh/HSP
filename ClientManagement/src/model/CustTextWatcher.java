package model;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.clientmanagement.WifiFragment;

public class CustTextWatcher implements TextWatcher {
	private String previousIter = "";
	String s;
	char thc;
	private Wrapper[] mass;
	private Cursor cursor;
	EditText er;

	public CustTextWatcher(EditText et, String Text) {
		mass = new Wrapper[] { new Wrapper(true), new Wrapper(), new Wrapper(),
				new Wrapper() };
		cursor = new Cursor(mass);
		er = et;
		er.setText(Text);
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String th = s.toString().trim();
		if (th.equals(previousIter.trim()))
			return;
		if (th.equals(WifiFragment.GlobalAddress))
			return;
		if (th.equals("")) {
			mass = new Wrapper[] { new Wrapper(true), new Wrapper(),
					new Wrapper(), new Wrapper() };
			cursor = new Cursor(mass);
			previousIter = "";
			return;
		}
		if (count > 1) {
			String newString = th.replaceAll("\\.", "");
			for (int i = 0; i < newString.length(); i++) {
				cursor.calculiteIndex(i, 0);
				cursor.add(newString.charAt(i));
			}
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < 4; i++) {
				if (mass[i].size == 0)
					break;
				str.append(mass[i]);
			}

			previousIter = str.toString();
			er.setText(previousIter);
			return;
		}
		if (count == 0) {
			if (cursor.getCount() == 0){
				er.setText("");
			}else
				cursor.remove();
		} else {
			int coun = 0;
			String the = previousIter.trim().substring(
					0,
					(start + 1 > previousIter.trim().length()) ? previousIter
							.trim().length() : start + 1);
			int st = -1;
			while ((st = the.indexOf(".", st + 1)) > 0) {
				coun++;
			}
			cursor.calculiteIndex(start, coun);
			thc = s.charAt(start);
			cursor.add(thc);
		}

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			if (mass[i].size == 0)
				break;
			str.append(mass[i]);
		}

		previousIter = str.toString();
		er.setText(previousIter);
		if (start + 1 == th.length() || before > 0) {
			er.setSelection(er.getText().toString().trim().length());
		} else {
			er.setSelection(start);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
