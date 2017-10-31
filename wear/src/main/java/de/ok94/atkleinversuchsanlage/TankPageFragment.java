package de.ok94.atkleinversuchsanlage;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TankPageFragment extends Fragment{

    TextView level1View, level2View, level3View, ll1View, ll2View, ll3View, lh1View, lh2View, lh3View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tank_page, container, false);

        level1View = (TextView) rootView.findViewById(R.id.level1);
        level2View = (TextView) rootView.findViewById(R.id.level2);
        level3View = (TextView) rootView.findViewById(R.id.level3);
        ll1View = (TextView) rootView.findViewById(R.id.ll1);
        ll2View = (TextView) rootView.findViewById(R.id.ll2);
        ll3View = (TextView) rootView.findViewById(R.id.ll3);
        lh1View = (TextView) rootView.findViewById(R.id.lh1);
        lh2View = (TextView) rootView.findViewById(R.id.lh2);
        lh3View = (TextView) rootView.findViewById(R.id.lh3);

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

    public void setCapacitiveSensorStates(boolean ll1, boolean ll2, boolean ll3, boolean lh1, boolean lh2, boolean lh3) {
        if (ll1) {
            ll1View.setBackgroundColor(getResources().getColor(R.color.light_grey, getActivity().getTheme()));
        }
        else {
            ll1View.setBackgroundColor(getResources().getColor(R.color.dark_grey, getActivity().getTheme()));
        }

        if (ll2) {
            ll2View.setBackgroundColor(getResources().getColor(R.color.light_grey, getActivity().getTheme()));
        }
        else {
            ll2View.setBackgroundColor(getResources().getColor(R.color.dark_grey, getActivity().getTheme()));
        }

        if (ll3) {
            ll3View.setBackgroundColor(getResources().getColor(R.color.light_grey, getActivity().getTheme()));
        }
        else {
            ll3View.setBackgroundColor(getResources().getColor(R.color.dark_grey, getActivity().getTheme()));
        }

        if (lh1) {
            lh1View.setBackgroundColor(getResources().getColor(R.color.light_grey, getActivity().getTheme()));
        }
        else {
            lh1View.setBackgroundColor(getResources().getColor(R.color.dark_grey, getActivity().getTheme()));
        }

        if (lh2) {
            lh2View.setBackgroundColor(getResources().getColor(R.color.light_grey, getActivity().getTheme()));
        }
        else {
            lh2View.setBackgroundColor(getResources().getColor(R.color.dark_grey, getActivity().getTheme()));
        }

        if (lh3) {
            lh3View.setBackgroundColor(getResources().getColor(R.color.light_grey, getActivity().getTheme()));
        }
        else {
            lh3View.setBackgroundColor(getResources().getColor(R.color.dark_grey, getActivity().getTheme()));
        }
    }
}
