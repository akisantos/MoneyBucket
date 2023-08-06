package com.akistd.moneybucket.ui.transaction;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.util.UtilConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTieuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTieuFragment extends Fragment {

    Button saveBtn, btnDatePicker;
    Spinner btnAllJam;
    ImageButton imgBtnOut;
    EditText editTotalMoney, editDescribe;
    private int mYear, mMonth, mDay;
    JarsChiTieuFragSpinnerAdapter adapter;
    ArrayList<Jars> jarsList = MongoDB.getInstance().getAllJars();
    Calendar currentDate = Calendar.getInstance();

    public ChiTieuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiTieuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiTieuFragment newInstance(String param1, String param2) {
        ChiTieuFragment fragment = new ChiTieuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_chi_tieu, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgBtnOut = view.findViewById(R.id.imgBtnOut);
        btnAllJam = view.findViewById(R.id.btnAllJam);
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        editTotalMoney = view.findViewById(R.id.editTotalMoney);
        editDescribe = view.findViewById(R.id.editDescribe);
        saveBtn = view.findViewById(R.id.saveBtn);

        addEvent();
    }

    private void addEvent() {

        jarListsSpinnerEvents();

        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        btnDatePicker.setText(UtilConverter.getInstance().vnTimeLocaleConverter(currentDate.getTime()));
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btnDatePicker.setText(UtilConverter.getInstance().vnTimeLocaleConverter(currentDate.getTime()));
                                currentDate.set(year,monthOfYear,dayOfMonth);
                            }

                        }, mYear, mMonth, mDay);


                datePickerDialog.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvents();
            }
        });


    }

    private void saveEvents() {
        createNewOutcomeTransaction();
    }

    private void createNewOutcomeTransaction() {

        if (dataValidator()) {
            //Lấy jar được chọn
            Jars selectedJars = (Jars) btnAllJam.getSelectedItem();

            //Lóc cóc tính tiền!
            Double newBalance = Double.parseDouble(String.valueOf(editTotalMoney.getText()));

            // Nếu tiền trong tài khoản nhiều hơn số tiền chi tiêu thì có thể tạo giao dịch
            if (selectedJars.getJarBalance() - newBalance >= 0) {

                //thu nhập lúc đầu = 100
                //chi tiêu 60 -> chi tiêu hơn 50 %
                //so sánh 100/2 lớn hoặc nhỏ hơn chi tiêu là ra
                Double halfJarMoney = calHalfJarAmountFromLatestIncome(selectedJars.getJarBalance());
                if (halfJarMoney > selectedJars.getJarBalance() - newBalance && selectedJars.getJarBalance() > halfJarMoney) {
                    sendNotification("Nhắc nhở!", "Bạn vừa dùng hơn 75% tổng số tiền trong hũ " + selectedJars.getJarName()+ ".(Từ lần nạp gần nhất)");
                }
                //Tạo transaction mới
                Transaction newIncome = new Transaction();
                newIncome.setOwner_id(MongoDB.getInstance().getUser().getId());
                newIncome.setCreateAt(new Date((currentDate.getTime()).getTime()));
                newIncome.setTransAmount(-newBalance);
                newIncome.setUser(MongoDB.getInstance().getMoneyUsers());
                newIncome.setJars(selectedJars);
                newIncome.setTransNote(String.valueOf(editDescribe.getText()));
                MongoDB.getInstance().insertTransaction(newIncome);


                // Update lại hũ
                if (MongoDB.getInstance().checkTransactionIsExists(newIncome).size()>0){
                    Jars modifyJar = new Jars(selectedJars);
                    modifyJar.setJarBalance(modifyJar.getJarBalance() + newIncome.getTransAmount());
                    MongoDB.getInstance().updateJar(modifyJar);
                }else{
                    Toast.makeText(getContext(),"Cập nhật không thành công, xin thử lại sau!", Toast.LENGTH_SHORT).show();
                }


                //reload main
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "You'r totally ran out of money! Beggar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Double calHalfJarAmountFromLatestIncome(Double currentAmount){
        Jars selectedJars = (Jars) btnAllJam.getSelectedItem();
        ArrayList<Transaction> transactions = MongoDB.getInstance().getAllSortedTransactionOfJar(selectedJars);
        Double halfJar = 0d;
        for (int i=0; i< transactions.size(); i++){
            if (transactions.get(i).getTransAmount()>0){
                halfJar = transactions.get(i).getTransAmount();

                break;
            }
        }
        Log.v("HET TIEN HET BAC LA CON NGUOI A LUC TRUOC", String.valueOf(halfJar));
        return  halfJar *0.75;
    }

    private void sendNotification(String title, String content) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.applogo);

        Notification notification = new NotificationCompat.Builder(requireActivity(), NotificationChannelClass.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(IconCompat.createWithBitmap(bitmap))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(getResources().getColor(R.color.teal_200))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(requireActivity());
        //check user permission for push notification
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(getNotificationId(), notification);
    }

    public int getNotificationId() {
        return (int) new Date().getTime();
    }

    private boolean dataValidator(){
        if (editTotalMoney.getText().length()>0){
            return true;
        }

        return false;
    }

    private void jarListsSpinnerEvents(){
        adapter = new JarsChiTieuFragSpinnerAdapter(getContext(), jarsList);
        adapter.setDropDownViewResource(R.layout.jarlist_chitieu_frag_dropdown);
        btnAllJam.setAdapter(adapter);

    }
}