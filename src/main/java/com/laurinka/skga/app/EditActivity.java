package com.laurinka.skga.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.laurinka.skga.app.storage.StorageHelper;
import com.ubikod.capptain.android.sdk.activity.CapptainListActivity;

/**
 * Backs edit screen.
 * 
 * @author radimpavlicek
 * 
 */
public class EditActivity extends CapptainListActivity {
	private List<String> data;

	private ArrayAdapter<String> adapter;
	private SharedPreferences sharedPreferences;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Setting a custom layout for the list activity */
		setContentView(R.layout.edit);
		sharedPreferences = getSharedPreferences(Constants.DATA_PREFERENCES,
				MODE_PRIVATE);
		showList();
	}

	public void close(View view) {
		sendBroadcast(new Intent(Constants.COM_LAURINKA_SKGA_APP_REFRESH));
		finish();
	}

	private void showList() {
		findData();
		/** Defining the ArrayAdapter to set items to ListView */
		adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_multiple_choice, data);
		// Assigned a custom layout located in layout #davidm

		// Assign adapter to ListView
		setListAdapter(adapter);
	}

	private void findData() {
		List<String> numbers = StorageHelper.getSkgaNumbers(sharedPreferences);
		String prefix = Constants.SKGA_NAME_PREFIX;
		ArrayList<String> data = grapNamesByPrefix(numbers, prefix);
		
		List<String> cgf = StorageHelper.getCgfNumbers(sharedPreferences);
		String prefixCgf = Constants.CGF_NAME_PREFIX;
		ArrayList<String> data2 = grapNamesByPrefix(cgf, prefixCgf);
		List<String> l = new LinkedList<String>();
		l.addAll(data);
		l.addAll(data2);
		this.data = l;
	}

	private ArrayList<String> grapNamesByPrefix(List<String> numbers,
			String prefix) {
		ArrayList<String> data = new ArrayList<String>();
		String[] numbersA = numbers.toArray(new String[0]);
		for (int i = 0; i < numbers.size(); i++) {
			String value = numbersA[i];
			String name = sharedPreferences.getString(prefix
					+ value, "");
			data.add(name);

		}
		return data;
	}

	/**
	 * Select all.
	 * @param view view
	 */
	public void invert(View view) {
		ListView lv = getListView();
		boolean check = lv.isItemChecked(0);
		int size = getListAdapter().getCount();
		for (int i = 0; i <= size; i++) {
			lv.setItemChecked(i, !check);
		}
	}

	public void delete(View view) {
		SparseBooleanArray checkedItemPositions = getListView()
				.getCheckedItemPositions();
		int itemCount = getListView().getCount();
		List<String> numbers = StorageHelper.getNumbers(sharedPreferences);
		List<String> numbersCgf = StorageHelper.getCgfNumbers(sharedPreferences);
		for (int i = itemCount - 1; i >= 0; i--) {
			if (checkedItemPositions.get(i)) {
				adapter.remove(data.get(i));
				getListView().setItemChecked(i, false);
				String toBeDeleted = numbers.get(i);
                if (numbersCgf.contains(toBeDeleted)) {
                    StorageHelper.removeCgfNumber(sharedPreferences, toBeDeleted);
                } else {
                    StorageHelper.removeSkgaNumber(sharedPreferences, toBeDeleted);
                }
			}
		}
		adapter.notifyDataSetChanged();
	}
}
