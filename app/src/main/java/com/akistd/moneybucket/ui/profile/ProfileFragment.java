package com.akistd.moneybucket.ui.profile;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.ui.transaction.ThuNhapJarsSettingsActivity;
import com.akistd.moneybucket.util.Constants;
import com.akistd.moneybucket.util.ImageLoadTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView profile_username,profile_userid;
    ImageView profile_image;
    ImageButton profile_logoutBtn;
    Constants util = new Constants();
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button deleteInfoBtn, sampleButtonID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        addControls(v);
        addEvents();
        return v;
    }

    private void addControls(View v){
        profile_username = v.findViewById(R.id.profile_username);
        profile_userid = v.findViewById(R.id.profile_userid);
        profile_logoutBtn = v.findViewById(R.id.profile_logoutBtn);
        profile_image = v.findViewById(R.id.profile_image);
        deleteInfoBtn  = v.findViewById(R.id.deleteInfoBtn);
        sampleButtonID = v.findViewById(R.id.sampleButtonID);
    }

    private void addEvents(){

        //from sampleButtonID go to ThuNhapJarsSettingsActivity activity
        sampleButtonID.setOnClickListener(v -> {
            Intent jarSettings = new Intent(getContext(), ThuNhapJarsSettingsActivity.class);
            startActivity(jarSettings);
        });

        //Lấy thông tin đăng nhập
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(util.getClientID()).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(),gso);
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getContext());
        acc.getPhotoUrl();

        //User profile info
        profile_username.setText(getArguments().getString("username"));
        profile_userid.setText(getArguments().getString("userId"));
        new ImageLoadTask(acc.getPhotoUrl().toString(),profile_image).execute();
        profile_logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        deleteInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserInfo();
            }
        });

    }

    private void deleteUserInfo(){
        ArrayList<Jars> allJars = MongoDB.getInstance().getAllJars();
        ArrayList<Transaction> allTrans = MongoDB.getInstance().getAllTransaction();

        for (Transaction tr: allTrans) {
            MongoDB.getInstance().deleteTransaction(tr.getId());
        }
        for (Jars j:allJars) {
            MongoDB.getInstance().deleteJar(j.getId());
        }
    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                doRestart(getContext());
            }
        });
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }

}