package yv.recipe.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import yv.recipe.R;

public class TabRecipes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_recipes,container,false);

        ImageView chefIcon = (ImageView) v.findViewById(R.id.analyze_icon);

        chefIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = new Toast(getActivity());
                t.setText("aaaaaaaaaaaa");
                t.setDuration(Toast.LENGTH_LONG);
                t.show();
            }
        });

        return v;
    }


}