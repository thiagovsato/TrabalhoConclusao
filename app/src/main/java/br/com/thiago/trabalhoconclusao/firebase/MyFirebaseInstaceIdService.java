package br.com.thiago.trabalhoconclusao.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstaceIdService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstaceIdService.class.getName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d(TAG, "Refreshed Token: " + refreshedToken);
    }
}
