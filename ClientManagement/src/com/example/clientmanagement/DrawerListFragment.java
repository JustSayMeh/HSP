package com.example.clientmanagement;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerListFragment extends ListFragment {
	FragmentClass[] mass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView mDrawerListView = (ListView) inflater.inflate(
				R.layout.list_fragment, container, false);
		mass = new FragmentClass[] {
				new FragmentClass("Управление", WifiFragment.class),
				new FragmentClass("Опции", OptionsFragment.class)};
		mDrawerListView.setAdapter(new ArrayAdapter<FragmentClass>(
				getActivity(), android.R.layout.simple_list_item_1, mass));
		return mDrawerListView;
	}

	@Override
	public void onListItemClick(ListView i, View v, int pos, long id) {
		FragmentClass th = mass[pos];
		try {

			MainActivity.fm.popBackStack();
				MainActivity.fm.beginTransaction()
						.replace(R.id.container, th.getFClass().newInstance())
						.commit();
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		MainActivity.drawerLayout.closeDrawers();
	}

	class FragmentClass {
		private Class<? extends Fragment> th;
		private String message;

		public FragmentClass(String mess, Class<? extends Fragment> cl) {
			th = cl;
			message = mess;
		}

		@Override
		public String toString() {
			return message;
		}

		public Class<? extends Fragment> getFClass() {
			return th;
		}
	}

}
