package yv.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;

import yv.recipe.adapters.ViewPagerAdapter;
import yv.recipe.utils.CameraUtils;


public class MainActivity extends AppCompatActivity implements ImageChooserListener {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    TabLayout tabs;
    ImageButton FAB;
    CharSequence tabsTitles[] = {"", ""};
    private Uri imageUri;
    private ImageChooserManager imageChooserManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tabsTitles[0] = getResources().getString(R.string.tab1_title);
        tabsTitles[1] = getResources().getString(R.string.tab2_title);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), tabsTitles, tabsTitles.length);



        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (TabLayout) findViewById(R.id.tabs);

        FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();

            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setupWithViewPager(pager);

        unregisterForContextMenu(FAB);

        registerForContextMenu(FAB);

//        new GcmRegistrationAsyncTask(this).execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

//        menu.setHeaderTitle("Add Your Photo");
        menu.add(1, 1, 1, getResources().getString(R.string.take_photo));
        menu.add(1, 2, 2, getResources().getString(R.string.choose_from_gallery));
        menu.add(1, 3, 3, getResources().getString(R.string.cancel));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // case take photo selected
        if(item.getItemId() == 1) {
            try {
                imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE);
                imageChooserManager.setImageChooserListener(this);
                imageChooserManager.choose();
            }
            catch (Exception e) {
                Snackbar.make(tabs, getResources().getString(R.string.error_take_photo), Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        // case select existing photo selected
        else if(item.getItemId() == 2) {
            try {
                imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE);
                imageChooserManager.setImageChooserListener(this);
                imageChooserManager.choose();
            }
            catch (Exception e) {
                Snackbar.make(tabs, getResources().getString(R.string.error_select_photo), Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        // case cancel selected
        else if(item.getItemId() == 3) {
            closeContextMenu();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE ||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            imageChooserManager.submit(requestCode, data);

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("opencv", "opencv success");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView userImage = (ImageView) findViewById(R.id.image_placeholder);
                TextView uploadImage = (TextView) findViewById(R.id.noImageText);
                TextView analyzeText = (TextView) findViewById(R.id.analyze_text);
                ImageView analyzeIcon = (ImageView) findViewById(R.id.analyze_icon);

                imageUri = Uri.parse(new File(image.getFileThumbnail()).toString());
                userImage.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
                userImage.setVisibility(View.VISIBLE);
                analyzeIcon.setVisibility(View.VISIBLE);
                analyzeText.setVisibility(View.VISIBLE);
                uploadImage.setVisibility(View.GONE);

                // set grayscale
                Drawable imgDrawable = userImage.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();
                CameraUtils.getInstance().convertToGrayScale(bitmap, userImage);
            }
        });

    }

    @Override
    public void onError(String s) {

    }
}
