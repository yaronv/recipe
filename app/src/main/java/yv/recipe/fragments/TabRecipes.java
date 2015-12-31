package yv.recipe.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import yv.recipe.R;

public class TabRecipes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_recipes,container,false);

        final ImageView analyzeIcon = (ImageView) v.findViewById(R.id.analyze_icon);
        final ImageView viewImage = (ImageView) v.findViewById(R.id.image_placeholder);
        final TextView analyzeText = (TextView) v.findViewById(R.id.analyze_text);
        final ProgressWheel loader = (ProgressWheel) v.findViewById(R.id.analyze_progress);

        analyzeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, getResources().getString(R.string.analyze_photo), Snackbar.LENGTH_LONG).show();

                analyzeIcon.setVisibility(View.GONE);
                analyzeText.setVisibility(View.GONE);

                loader.setVisibility(View.VISIBLE);

                analyzePhoto(viewImage);
            }
        });

        return v;
    }



    public void analyzePhoto(ImageView viewImage) {
//        Drawable imgDrawable = viewImage.getDrawable();
//        Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

//                Bitmap bitmap = BitmapFactory.decodeResource(viewImage.getResources(), R.id.image_placeholder);
//        CameraUtils.getInstance().analyzeImage(bitmap, viewImage);
    }

}