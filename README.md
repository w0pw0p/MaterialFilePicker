# MaterialFilePicker
An Android material-designed library for picking files on internal and external storage

<p>
<img src="http://i.imgur.com/b9iTvLZ.png"
width="25%"
</img>

<img src="http://i.imgur.com/lq0xYGa.png"
width="25%"
</img>
</p>

<p>
<img src="http://i.imgur.com/DaxBOV1.png"
width="25%"
</img>

<img src="http://i.imgur.com/tD8ZZeM.png"
width="25%"
</img>
</p>

## Features
<br>~   Thumbnails for images
<br>~   Can select multiple items
<br>~   AutoScroll to last scroll position on back pressed
<br>~   Create new directories in the picker
<br>~   Switch storage from internal to external and vice versa
<br>~   Material theme using AppCompat
<br>~   Drawables change colors based on theme

## How to include in your project (with Gradle)

Copy library to your application's folder then add the dependency to your *build.gradle*:
```groovy
dependencies {
  compile fileTree(dir: 'libs', include: ['*.jar'])
  compile project(':MaterialFilePickerLibrary')
}
```

## How to use this library

### Include the file picker activity

Note that the theme should be set in the manifest.

```xml
    <activity
        android:name="spt.w0pw0p.material.filepicker.FileSelectionActivity"
			  android:theme="@style/AppTheme" >
    </activity>
```

### Configure the theme

You must **set the theme** on the activity, but you can configure it to
match your preference. Material colors are included in the library.

```xml
    <style name="AppTheme" parent="Theme.AppCompat">
        <!-- Customize your theme here. -->
		<item name="colorPrimary">@color/cyan_500</item>
		<item name="colorPrimaryDark">@color/cyan_700</item>
		<item name="colorAccent">@color/cyan_300</item>
		<item name="alertDialogTheme">@style/filePickerAlertDialogTheme</item>
		<item name="actionMenuTextAppearance">@style/actionBarMenuTextSize</item>
    </style>

	<style name="filePickerAlertDialogTheme" parent="Theme.AppCompat.Dialog.Alert" />

	<style name="actionBarMenuTextSize" parent="Base.TextAppearance.AppCompat.Menu">
		<item name="android:textSize">12sp</item>
	</style>
```

### Starting the picker in your app

First import the library
```java
import spt.w0pw0p.material.filepicker.FileSelectionActivity;
```

Use this code to start the file picker. Change EXTRA_CREATE_FOLDER to true if you want
to enable folder creation on the file picker.
```java
Intent intent = new Intent(getBaseContext(), FileSelectionActivity.class);
intent.putExtra(FileSelectionActivity.EXTRA_CREATE_FOLDER, false);
startActivityForResult(intent, 0);
```
	
Use this code to capture the results.

```java
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
				// Do something on the results
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
				// Do something on the results
			}
		}
		else
		{
			if (data != null)
			{
				// data contains the results
			}
		}
	}
```
		
### Credits

<br>~ Base library        - <a href="https://github.com/paulasiimwe/Android-Multiple-file-Selector-Dialog" target="_blank">Android-Multiple-file-Selector-Dialog</a>
<br>~ Libraries included  - <a href="https://github.com/square/picasso" target="_blank">Picasso</a>, <a href="https://github.com/commonsguy/cwac-merge" target="_blank">CommonsWare Android Components: MergeAdapter</a>
<br>~ Icons used        - <a href="https://materialdesignicons.com/" target="_blank">Material Design Icons</a>
<br>~ Other codes used    - <a href="https://github.com/spacecowboy/NoNonsense-FilePicker" target="_blank">NoNonsense-FilePicker</a>
