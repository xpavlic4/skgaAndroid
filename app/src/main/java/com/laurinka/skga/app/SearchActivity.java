package com.laurinka.skga.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.laurinka.skga.app.util.ResourceHelper;
import com.ubikod.capptain.android.sdk.activity.CapptainActivity;
/**
 * Backs search by name screen.
 * @author radimpavlicek
 *
 */
public class SearchActivity extends CapptainActivity {

	private String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		Bundle extras = getIntent().getExtras();
		type = extras.getString("type");

	}

	public void close(View view) {
		finish();
	}
	
	public void startSearch(View view) {
		String message = findSearchPattern();
        message = message.replace("\n", "").replace("\r", "").replaceAll("[^\\w\\s]","");
        message = ResourceHelper.stripAccents(message);
		if (null == message|| message.length() < 2) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setMessage(getString(R.string.min_2_characters));
			alertDialog.setPositiveButton(R.string.back_to_search_button, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                  dialog.dismiss();
	               }
	           });

			alertDialog.show();
			return;
		}
		Intent i  = new Intent(this, AddByNameActivity.class);
		i.putExtra("pattern", message);
		i.putExtra("type", type);
		startActivity(i);
	}

	private String findSearchPattern() {
		EditText editText = findEditText();
		return editText.getText().toString();
	}

	private EditText findEditText() {
		return (EditText) findViewById(R.id.edit_search);
	}
}
