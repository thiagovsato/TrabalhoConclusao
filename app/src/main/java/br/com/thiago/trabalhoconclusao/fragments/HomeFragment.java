package br.com.thiago.trabalhoconclusao.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.ProfileTracker;

import br.com.thiago.trabalhoconclusao.R;

public class HomeFragment extends Fragment {

    View view;
    TextView tvHomeHello, tvHomeMiddle, tvHomeBottom;
    private ProfileTracker mProfileTracker;

    public HomeFragment(){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.menu_home));

        tvHomeHello = (TextView) getActivity().findViewById(R.id.tvHomeHello);
        tvHomeMiddle = (TextView) getActivity().findViewById(R.id.tvHomeMiddle);
        tvHomeBottom = (TextView) getActivity().findViewById(R.id.tvHomeBottom);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ChocolateCoveredRaindropsBOLD.ttf");
        tvHomeHello.setTypeface(type);
        tvHomeMiddle.setTypeface(type);
        tvHomeBottom.setTypeface(type);

        String name = getString(R.string.hello) + ", ";


        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("Facebook_login")) {
                Profile userProfile = Profile.getCurrentProfile();

                if (userProfile != null) {
                    name = name + userProfile.getName();
                    tvHomeHello.setText(name);
                } else {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            tvHomeHello.setText(getString(R.string.hello) + " " + profile2.getName());
                            mProfileTracker.stopTracking();
                        }};}
            } else if (extras.containsKey("Normal_login")){
                name = name+ intent.getStringExtra("Normal_login");
                tvHomeHello.setText(name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

}
