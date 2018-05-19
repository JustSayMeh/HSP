package com.example.clientmanagement;

import model.CustTextWatcher;
import model.OptionsSaver;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class OptionsFragment extends Fragment {
	private int port = -1, timeout;
	private String defAdr = "-1";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.options, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		defAdr = OptionsSaver.getAddr();
		port = OptionsSaver.getPort();
		timeout = OptionsSaver.getTimeout();
		final EditText portEd = (EditText) view.findViewById(R.id.port);
		portEd.setText(String.valueOf(port));
		final EditText addrEd = (EditText) view.findViewById(R.id.Address);
		addrEd.addTextChangedListener(new CustTextWatcher(addrEd, defAdr));
		final EditText timeOut = (EditText) view.findViewById(R.id.timeout);
		timeOut.setText(String.valueOf(timeout));
		Button but = (Button) view.findViewById(R.id.accept);
		but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				port = Integer.valueOf(portEd.getText().toString());
				defAdr = addrEd.getText().toString();
				timeout = Integer.valueOf(timeOut.getText().toString());
				OptionsSaver.setAddr(defAdr);
				OptionsSaver.setPort(port);
				OptionsSaver.setTimeout(timeout);
				OptionsSaver.save(getActivity());
				Toast.makeText(getActivity(), "Настройки сохранены", 1500)
						.show();
			}
		});
		super.onViewCreated(view, savedInstanceState);
	}

}
