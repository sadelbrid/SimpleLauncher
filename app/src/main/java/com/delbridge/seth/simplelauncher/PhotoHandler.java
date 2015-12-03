package com.delbridge.seth.simplelauncher;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Seth on 12/2/15.
 */
public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
        Home.pictureDir = getDir().getPath() + File.separator+"LockScreenSecurityWarning.jpg";
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i("CameraHome", "Picture Taken");
        File pictureFileDir = getDir();


        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();

            return;

        }

        String photoFile = "LockScreenSecurityWarning.jpg";
        String filename = pictureFileDir.getPath() + File.separator + photoFile;
        //Home.pictureDir = filename;
        Log.i("picturepath", filename);
        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(context, "New Image saved:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "LockScreenSecurity");
    }
}
