package de.ok94.atkleinversuchsanlage;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PumpPageFragment extends Fragment{

    private ImageView imagePumpingButton;
    private TextView textPumpingButton;
    private TextView textPumpingState;
    private LinearLayout pumpingButton;

    private boolean pumping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pump_page, container, false);

        imagePumpingButton = (ImageView) rootView.findViewById(R.id.imagePumpingButton);
        textPumpingButton = (TextView) rootView.findViewById(R.id.textPumpingButton);
        textPumpingState = (TextView) rootView.findViewById(R.id.textPumpingState);
        pumpingButton = (LinearLayout) rootView.findViewById(R.id.pumpingButton);

        pumping = false;

        pumpingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pumping) {
                    SoapWriteTask soapWriteTask = new SoapWriteTask(false, 0, 0);
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
            imagePumpingButton.setImageResource(R.drawable.ic_action_stop);
            textPumpingButton.setText(R.string.stop);
            String pumpingState = getString(R.string.pumping_active);
            pumpingState = String.format(pumpingState, tankA, tankB);
            textPumpingState.setText(pumpingState);
        }
        else {
            imagePumpingButton.setImageResource(R.drawable.ic_action_start);
            textPumpingButton.setText(R.string.start);
            textPumpingState.setText(R.string.pumping_inactive);
        }
    }
}
