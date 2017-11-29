package de.ok94.atkleinversuchsanlage;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TankPageFragment extends Fragment{

    private TextView titleView;
    private TextView tank1View, tank2View, tank3View;
    private TextView level1View, level2View, level3View;
    private TextView textTank1, textTank2, textTank3;
    private ImageView warning1View, warning2View, warning3View;

    private boolean ll1old, ll2old, ll3old, lh1old, lh2old, lh3old;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tank_page, container, false);

        titleView = (TextView) rootView.findViewById(R.id.tankTitle);
        tank1View = (TextView) rootView.findViewById(R.id.tank1);
        tank2View = (TextView) rootView.findViewById(R.id.tank2);
        tank3View = (TextView) rootView.findViewById(R.id.tank3);
        level1View = (TextView) rootView.findViewById(R.id.level1);
        level2View = (TextView) rootView.findViewById(R.id.level2);
        level3View = (TextView) rootView.findViewById(R.id.level3);
        textTank1 = (TextView) rootView.findViewById(R.id.textTank1);
        textTank2 = (TextView) rootView.findViewById(R.id.textTank2);
        textTank3 = (TextView) rootView.findViewById(R.id.textTank3);
        warning1View = (ImageView) rootView.findViewById(R.id.warning1);
        warning2View = (ImageView) rootView.findViewById(R.id.warning2);
        warning3View = (ImageView) rootView.findViewById(R.id.warning3);

        ll1old = true;
        ll2old = true;
        ll3old = true;
        lh1old = false;
        lh2old = false;
        lh3old = false;

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
        if (lh1 || !ll1) {
            warning1View.setVisibility(View.VISIBLE);
            if (lh1 != lh1old || ll1 != ll1old) {
                vibrate();
            }
        }
        else {
            warning1View.setVisibility(View.INVISIBLE);
        }
        if (lh2 || !ll2) {
            warning2View.setVisibility(View.VISIBLE);
            if (lh2 != lh2old || ll2 != ll2old) {
                vibrate();
            }
        }
        else {
            warning2View.setVisibility(View.INVISIBLE);
        }
        if (lh3 || !ll3) {
            warning3View.setVisibility(View.VISIBLE);
            if (lh3 != lh3old || ll3 != ll3old) {
                vibrate();
            }
        }
        else {
            warning3View.setVisibility(View.INVISIBLE);
        }

        ll1old = ll1;
        ll2old = ll2;
        ll3old = ll3;
        lh1old = lh1;
        lh2old = lh2;
        lh3old = lh3;
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 300};
        // -1 - don't repeat
        final int indexInPatternToRepeat = -1;
        try {
            vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
        }
        catch (Exception e) {
            Log.e("VIBRATION", e.toString());
        }
    }

    public void enterAmbient() {
        final Drawable BACKGROUND = getResources().getDrawable(R.drawable.rect_border, getActivity().getTheme());
        titleView.setTextColor(Color.WHITE);
        tank1View.setBackground(BACKGROUND);
        tank2View.setBackground(BACKGROUND);
        tank3View.setBackground(BACKGROUND);
        level1View.setBackground(BACKGROUND);
        level2View.setBackground(BACKGROUND);
        level3View.setBackground(BACKGROUND);
        textTank1.setTextColor(Color.WHITE);
        textTank2.setTextColor(Color.WHITE);
        textTank3.setTextColor(Color.WHITE);
    }

    public void exitAmbient() {
        final int ACCENT = getResources().getColor(R.color.accent, getActivity().getTheme());
        final int DARK_UI = getResources().getColor(R.color.darker_ui_element, getActivity().getTheme());
        final int ACTIVE_UI = getResources().getColor(R.color.active_ui_element, getActivity().getTheme());
        final ColorDrawable DARK_UI_BACKGROUND = new ColorDrawable(DARK_UI);
        final ColorDrawable ACTIVE_UI_BACKGROUND = new ColorDrawable(ACTIVE_UI);
        titleView.setTextColor(ACCENT);
        tank1View.setBackground(DARK_UI_BACKGROUND);
        tank2View.setBackground(DARK_UI_BACKGROUND);
        tank3View.setBackground(DARK_UI_BACKGROUND);
        level1View.setBackground(ACTIVE_UI_BACKGROUND);
        level2View.setBackground(ACTIVE_UI_BACKGROUND);
        level3View.setBackground(ACTIVE_UI_BACKGROUND);
        textTank1.setTextColor(ACCENT);
        textTank2.setTextColor(ACCENT);
        textTank3.setTextColor(ACCENT);
    }
}
