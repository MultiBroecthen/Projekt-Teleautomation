package de.ok94.atkleinversuchsanlage;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TankPageFragment extends Fragment{

    TextView level1View, level2View, level3View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tank_page, container, false);

        level1View = (TextView) rootView.findViewById(R.id.level1);
        level2View = (TextView) rootView.findViewById(R.id.level2);
        level3View = (TextView) rootView.findViewById(R.id.level3);

        return rootView;
    }

    public void setTankLevels(float level1, float level2, float level3) {
        level1View.getLayoutParams().height = (int) level1;
        level2View.getLayoutParams().height = (int) level2;
        level3View.getLayoutParams().height = (int) level3;

        level1View.requestLayout();
        level2View.requestLayout();
        level3View.requestLayout();
    }
}
