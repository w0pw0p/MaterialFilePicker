package spt.w0pw0p.material.filepicker.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import spt.w0pw0p.material.filepicker.FileSelectionActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity 
{

	Button mStart;
	CheckBox mEnable;
	TextView mResults;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		mStart = (Button) findViewById(R.id.mainButton1);
		mEnable = (CheckBox) findViewById(R.id.mainCheckBox1);
		mResults = (TextView) findViewById(R.id.mainTextView1);

		mStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
				{
                    // Perform action on click   
					Intent intent = new Intent(getBaseContext(), FileSelectionActivity.class);
					intent.putExtra(FileSelectionActivity.EXTRA_CREATE_FOLDER, mEnable.isChecked());
					startActivityForResult(intent, 0);
                }
            });

		mResults.setMovementMethod(new ScrollingMovementMethod());

    }


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        if (requestCode == 0 && resultCode == RESULT_OK)
		{

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			{
				ClipData clip = data.getClipData();
				StringBuilder sb = new StringBuilder();

				if (clip != null)
				{
					for (int i = 0; i < clip.getItemCount(); i++)
					{
						sb.append(clip.getItemAt(i).getUri().toString());
						sb.append("\n");
					}
				}
				mResults.setText(sb.toString());
			}
			else
			{
				ArrayList<String> paths = data.getStringArrayListExtra(
					FileSelectionActivity.EXTRA_PATHS);
				StringBuilder sb = new StringBuilder();

				if (paths != null)
				{
					for (String path : paths)
					{
						sb.append(path);
						sb.append("\n");
					}
				}
				mResults.setText(sb.toString());
			}
		}
		else
		{
			if (data != null)
			{
				mResults.setText(data.getData().toString());
			}
		}
	}

}
