package de.ok94.atkleinversuchsanlage;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PumpPageFragment extends Fragment{

    private TextView titleText;
    private LinearLayout pumpingButton;
    private ImageView pumpingButtonImage;
    private TextView pumpingButtonText;
    private TextView pumpingStateText;

    private boolean pumping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pump_page,
                container, false);

        titleText = (TextView) rootView.findViewById(R.id.text_pump_title);
        pumpingButtonImage = (ImageView) rootView.findViewById(R.id.image_toggle_pumping_button);
        pumpingButtonText = (TextView) rootView.findViewById(R.id.text_toggle_pumping_button);
        pumpingStateText = (TextView) rootView.findViewById(R.id.text_pumping_state);
        pumpingButton = (LinearLayout) rootView.findViewById(R.id.button_toggle_pumping);

        pumping = false;

        pumpingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pumping) {
                    SoapWriteTask soapWriteTask =
                            new SoapWriteTask(false, 0, 0);
                    soapWriteTask.execute();
                }
                else {
                    Intent intent = new Intent(getActivity(), ChooseSourceActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    public void setPumpingState(boolean pumping, int tankA, int tankB) {
        this.pumping = pumping;

        if (pumping) {
            pumpingButtonImage.setImageResource(R.drawable.ic_action_stop);
            pumpingButtonText.setText(R.string.stop);
            String pumpingState = getString(R.string.pumping_active);
            pumpingState = String.format(pumpingState, tankA, tankB);
            pumpingStateText.setText(pumpingState);
        }
        else {
            pumpingButtonImage.setImageResource(R.drawable.ic_action_start);
            pumpingButtonText.setText(R.string.start);
            pumpingStateText.setText(R.string.pumping_inactive);
        }
    }

    public void enterAmbient() {
        titleText.setTextColor(Color.WHITE);
        pumpingStateText.setTextColor(Color.WHITE);
        pumpingButton.setVisibility(View.INVISIBLE);
    }

    public void exitAmbient() {
        final int ACCENT = getResources()
                .getColor(R.color.accent, getActivity().getTheme());
        titleText.setTextColor(ACCENT);
        pumpingStateText.setTextColor(ACCENT);
        pumpingButton.setVisibility(View.VISIBLE);
    }
}
