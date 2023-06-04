package com.example.babyphone2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.babyphone2.R;
import com.example.babyphone2.Strings;
import com.example.babyphone2.models.Phone;

import java.util.List;

public class PhoneListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Phone> phones;
    /*private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    */

    private String userId;
    private Strings values;
    private Phone currentPhone;

    public PhoneListViewAdapter(Context context, List<Phone> phones){
        this.context = context;
        this.phones = phones;
        /*mAuth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef= database.getReference("Users");
        userId= mAuth.getCurrentUser().getUid();
        values = Strings.getInstance();
        currentPhone = getCurrentPhone(values.getPhoneName());
        */
    }

    @Override
    public int getCount() {
        return phones.size();
    }

    @Override
    public Object getItem(int i) {
        return phones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        Phone phone = phones.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.row, null, false);
        TextView phoneName = view.findViewById(R.id.phoneNameTv);
        phoneName.setText(phone.getPhoneName());
        return view;
    }
    public Phone getCurrentPhone(String phoneName){
        Phone currentPhone = new Phone();
        for (Phone phone:
             phones) {
            if(phone.getPhoneName() == phoneName){
                currentPhone = phone;
            }
        }
        return currentPhone;
    }

}
