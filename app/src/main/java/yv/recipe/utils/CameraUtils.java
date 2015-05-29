package yv.recipe.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class CameraUtils {

    private static CameraUtils instance = null;

    public static CameraUtils getInstance() {
        if(instance == null) {
            instance = new CameraUtils();
        }
        return instance;
    }

    private CameraUtils() {

    }

    public boolean isCameraSupported(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(context, "This device does not have a camera.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void analyzeImage(Bitmap bitmap, ImageView viewImage) {

//        bitmap.get
        Mat mat = Mat.zeros(viewImage.getHeight(), viewImage.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(mat, bitmap);
        viewImage.setImageBitmap(bitmap);
    }
}
