package spt.w0pw0p.material.filepicker;

/**
 * forked from https://github.com/paulasiimwe/Android-Multiple-file-Selector-Dialog
 * modified by w0pw0p
 */
 
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.commonsware.cwac.merge.MergeAdapter;

public class FileSelectionActivity extends AppCompatActivity {

    private static final String TAG = "FileSelection";
	public static final String EXTRA_CREATE_FOLDER = "fileselector.intent.CREATE_FOLDER";
	public static final String EXTRA_PATHS = "fileselector.intent.PATHS";
	
    private ArrayList<File> directoryList = new ArrayList<File>();
    private ArrayList<File> fileList = new ArrayList<File>();
    private ArrayList<String> directoryNames = new ArrayList<String>();
    private ArrayList<String> fileNames = new ArrayList<String>();
	private ArrayList<Uri> resultFileUri;
	private ListView directoryView;
	
	boolean createFolder = true;
	Boolean Switch = false;
	Boolean switcher = false;
    Button ok, all, cancel, storage , New;
	File mainPath = new File(Environment.getExternalStorageDirectory() + "");
    int index = 0;
    int top = 0;
    String primary_sd;
    String secondary_sd;
	TextView path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        
		Intent intent = getIntent();
		createFolder = intent.getBooleanExtra(EXTRA_CREATE_FOLDER, createFolder);

        directoryView = (ListView)findViewById(R.id.directorySelectionList);
        ok = (Button)findViewById(R.id.ok);
        all = (Button)findViewById(R.id.all);
        cancel = (Button)findViewById(R.id.cancel);
        path = (TextView)findViewById(R.id.folderpath);

        loadLists();

        directoryView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                index = directoryView.getFirstVisiblePosition();
                View v = directoryView.getChildAt(0);
                top = (v == null) ? 0 : v.getTop();

                File lastPath = mainPath;
                try {
                    if (position < directoryList.size()) {
                        mainPath = directoryList.get(position);
                        loadLists();
						getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                } catch (Throwable e){
                    mainPath = lastPath;
                    loadLists();
                }

            }
        });

        ok.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ok();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
 
        all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Switch) {
                    for (int i = directoryList.size(); i < directoryView.getCount(); i++){
                        directoryView.setItemChecked(i, true);
                    }
                    all.setText(getString(R.string.none));
					all.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_select_off_tinted, 0, 0, 0);
                    Switch = true;
                } else if (Switch){
                    for (int i = directoryList.size(); i < directoryView.getCount(); i++) {
                        directoryView.setItemChecked(i, false);
                    }
                    all.setText(getString(R.string.all));
					all.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_select_all_tinted, 0, 0, 0);
                    Switch = false;
                }
            }

        });
    }

	@Override
    public void onBackPressed() {
        goBack();
    }

	private void goBack() {
		try {
			if (mainPath.equals(new File(primary_sd)) || mainPath.equals(new File(Environment.getExternalStorageDirectory() + "")) || mainPath.equals(new File(secondary_sd))) {
                finish();
			} else if (mainPath.getParentFile().equals(new File(primary_sd)) || mainPath.getParentFile().equals(new File(Environment.getExternalStorageDirectory() + "")) || mainPath.getParentFile().equals(new File(secondary_sd))) {
				getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                File parent = mainPath.getParentFile();
                mainPath = parent;
                loadLists();
                directoryView.setSelectionFromTop(index, top);
            } else {
                File parent = mainPath.getParentFile();
                mainPath = parent;
                loadLists();
                directoryView.setSelectionFromTop(index, top);
            }

        } catch (Exception e){
			Log.d(TAG, e.toString());
        }
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void ok() {
      
		resultFileUri = new ArrayList<Uri>();

        for(int i = 0 ; i < directoryView.getCount(); i++){
            if(directoryView.isItemChecked(i)){
				resultFileUri.add(Uri.parse(fileList.get(i-directoryList.size()).getAbsolutePath()));
            }
        }
 
		if (resultFileUri.isEmpty()) {
			Log.d(TAG, "No selection.");
			finish();
		}
		
		// code from https://github.com/spacecowboy/NoNonsense-FilePicker
		Intent i = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip = null;
            for (Uri file : resultFileUri) {
                if (clip == null) {
                    clip = new ClipData("Paths", new String[]{},
										new ClipData.Item(file));
                } else {
                    clip.addItem(new ClipData.Item(file));
                }
            }
            i.setClipData(clip);
        } else {
            ArrayList<String> paths = new ArrayList<>();
            for (Uri file : resultFileUri) {
                paths.add(file.toString());
            }
            i.putStringArrayListExtra(EXTRA_PATHS, paths);
        }

        setResult(Activity.RESULT_OK, i);
        finish();
		
    }

    private void loadLists() {
		
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        };
		
        FileFilter directoryFilter = new FileFilter(){
            public boolean accept(File file){
                return file.isDirectory();
            }
        };

        //Lista de directorios
        File[] tempDirectoryList = mainPath.listFiles(directoryFilter);

        if (tempDirectoryList != null && tempDirectoryList.length > 1) {
            Arrays.sort(tempDirectoryList, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }

        directoryList = new ArrayList<File>();
        directoryNames = new ArrayList<String>();
        for(File file: tempDirectoryList){
            directoryList.add(file);
            directoryNames.add(file.getName());
        }
		
        ArrayAdapter<String> directoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directoryNames);

        //Lista de ficheros
        File[] tempFileList = mainPath.listFiles(fileFilter);

        if (tempFileList != null && tempFileList.length > 1) {
            Arrays.sort(tempFileList, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }

        fileList = new ArrayList<File>();
        fileNames = new ArrayList<String>();
        for(File file : tempFileList){
            fileList.add(file);
            fileNames.add(file.getName());
        }

        path.setText(mainPath.toString());
        iconload();
        setTitle(mainPath.getName());

    }

    public void iconload() {
		
        String[] foldernames = new String[directoryNames.size()];
        foldernames = directoryNames.toArray(foldernames);

        String[] filenames = new String[fileNames.size()];
        filenames = fileNames.toArray(filenames);
		
		MergeAdapter adap = new MergeAdapter();

		CustomListSingleOnly adapter1 = new CustomListSingleOnly(FileSelectionActivity.this, directoryNames.toArray(foldernames), mainPath.getPath());
		CustomList adapter2 = new CustomList(FileSelectionActivity.this, fileNames.toArray(filenames), mainPath.getPath());
		
		adap.addAdapter(adapter1);
		adap.addAdapter(adapter2);

        directoryView.setAdapter(adap);
    }

    public void ExtStorageSearch() {
		
        String[] extStorlocs = {"/storage/sdcard1","/storage/extsdcard","/storage/sdcard0/external_sdcard","/mnt/extsdcard",
                "/mnt/sdcard/external_sd","/mnt/external_sd","/mnt/media_rw/sdcard1","/removable/microsd","/mnt/emmc",
                "/storage/external_SD","/storage/ext_sd","/storage/removable/sdcard1","/data/sdext","/data/sdext2",
			"/data/sdext3","/data/sdext4","/storage/sdcard0"};

        //First Attempt
        primary_sd = System.getenv("EXTERNAL_STORAGE");
        secondary_sd = System.getenv("SECONDARY_STORAGE");


        if (primary_sd == null) {
            primary_sd = Environment.getExternalStorageDirectory() + "";
        }
		
        if (secondary_sd == null) {//if fail, search among known list of extStorage Locations
            for(String string: extStorlocs){
                if((new File(string)).exists() && (new File(string)).isDirectory() ){
                    secondary_sd = string;
                    break;
                }
            }
        }
    }
	
	private void createNewFolder() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle( getString(R.string.New) );
		alert.setMessage( getString(R.string.CNew) );

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String fileName = input.getText().toString();
					// Verify if a value has been entered.
					if(fileName != null && fileName.length() > 0) {
						// Notify the listeners.
						File newFolder = new File(mainPath.getPath()+"/"+fileName+"/");
						newFolder.mkdirs();
						loadLists();
					}
				}
			});
		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Do nothing, automatically the dialog is going to be closed.
				}
			});

		// Show the dialog.
		alert.show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.main, menu);
		ExtStorageSearch();
		
        if (secondary_sd == null) {
			
			//stackoverflow.com/questions/10692755/how-do-i-hide-a-menu-item-in-the-actionbar
			MenuItem item = menu.findItem(R.id.switch_storage);
            item.setVisible(false);
			this.invalidateOptionsMenu();
        }
		
		if (createFolder == false) {
			MenuItem item = menu.findItem(R.id.new_folder);
            item.setVisible(false);
			this.invalidateOptionsMenu();
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.new_folder) {
			createNewFolder();
			
		} else if (item.getItemId() == R.id.switch_storage) {
			try {
				if (!switcher) {
					mainPath = new File(secondary_sd);
					loadLists();
					switcher = true;
				} else {
					mainPath = new File(primary_sd);
					loadLists();
					switcher = false;
				}
			} catch (Exception e){
				Log.d(TAG, e.toString());
			}
		} else if (item.getItemId() == android.R.id.home) {
			goBack();
		}
		
		// TODO: Implement this method
		return super.onOptionsItemSelected(item);
	}

}
