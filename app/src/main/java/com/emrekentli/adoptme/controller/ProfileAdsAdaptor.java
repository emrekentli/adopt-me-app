package com.emrekentli.adoptme.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAdsAdaptor extends ArrayAdapter<PostModel> {
    List<PostModel> listData;
    Context context;
    int resource;
    FragmentActivity fragmentx;
    TokenManager tokenManager;


    public ProfileAdsAdaptor(@NonNull Context context, int resource, @NonNull List<PostModel> listData, FragmentActivity fragmentx) {

        super(context, resource, listData);


        this.context=getContext();
        this.resource=resource;
        this.listData=listData;
        this.fragmentx=fragmentx;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.profileads_row,null,false);
        }
        tokenManager = new TokenManager(getContext());
        PostModel listdata=getItem(position);
        Integer viewCount = 0;




        ImageView img = (ImageView) convertView.findViewById(R.id.ad_image);
        TextView txtStatus = convertView.findViewById(R.id.ad_status);
        TextView adDetail = convertView.findViewById(R.id.ad_Detail2);
        TextView adDetail2 = convertView.findViewById(R.id.ad_Detail);
        TextView txtName = convertView.findViewById(R.id.ad_Name);
        Button deleteBt = convertView.findViewById(R.id.deleteBt);
        TextView adViewValue = convertView.findViewById(R.id.ad_ViewValue);
        Boolean confirmation = listdata.getVerified();
        if (confirmation != null) {
            if (confirmation) {
                txtStatus.setText("YAYINDA");
            } else {
                txtStatus.setText("REDDEDİLDİ");
            }
        } else {
            txtStatus.setText("İNCELENİYOR.");
        }

        adViewValue.setText("Görüntülenme: " + String.valueOf(viewCount));

        txtName.setText(listdata.getName());
        adDetail2.setText("\n" + listdata.getDescription());
        adDetail.setText(listdata.getAnimalType().getName());
        String base64Image = listdata.getMainImage();
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));



           if (adDetail2.length()>50) {
          String detail=listdata.getDescription().substring(0,50)+"...";
            adDetail2.setText(detail);
              }


        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete(listdata.getId());
            }
        });




        return convertView;
    }


    public void replaceFragmentsAds(Class fragmentClass, String adid) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        Bundle args = new Bundle();
        args.putString("adid",adid);
        fragment.setArguments(args);
        FragmentManager fragmentManager = fragmentx.getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }


    public void delete (String id) {
        new AlertDialog.Builder(context)
                .setTitle("İlan Silme İşlemi")
                .setMessage("Bu ilanı silmek istediğinize emin misiniz?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Sil", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                        deleteAds(id);
                    }
                })

               .setNeutralButton ("İptal Et", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();




    }

    public void deleteAds (String id) {
        final Interface restInterface = new ApiClient().getClient().create(Interface.class);
        Call<Void> call = restInterface.deleteAds("Bearer " +tokenManager.getToken(),id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getContext(), "İlan başarıyla silindi.", Toast.LENGTH_SHORT).show();
                    for (PostModel posts : listData) {
                        if (posts.getId().equals(id)) {
                            listData.remove(posts);
                            notifyDataSetChanged();
                            break;
                        }
                    }

                } else {

                    Toast.makeText(getContext(), "İlan silinemedi.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Hata",t.toString());
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
