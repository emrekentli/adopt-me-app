package com.emrekentli.adoptme.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.model.PostModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchAdaptor extends ArrayAdapter<PostModel> {
    List<PostModel> listData;
    Context context;
    int resource;



    public SearchAdaptor(@NonNull Context context, int resource, @NonNull List<PostModel> listData) {
        super(context, resource, listData);


        this.context=context;
        this.resource=resource;
        this.listData=listData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.search_row,null,true);
        }
        PostModel listdata=getItem(position);
        ImageView img=(ImageView)convertView.findViewById(R.id.ad_image);
        TextView txtCountry=convertView.findViewById(R.id.ad_status);
        TextView txtName=convertView.findViewById(R.id.ad_Name);
        TextView txtDetail=convertView.findViewById(R.id.ad_Detail);
        TextView txtAltDetail=convertView.findViewById(R.id.ad_Detail2);

        txtAltDetail.setText(listdata.getBreed().getName());
        txtName.setText(listdata.getName());
        txtCountry.setText(listdata.getCity().getName());
        txtDetail.setText(formatDate(listdata.getCreated()));
        String base64Image = listdata.getMainImage();
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        img.setImageBitmap(originalBitmap);
        return convertView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return dateFormat.format(date);
    }

}
