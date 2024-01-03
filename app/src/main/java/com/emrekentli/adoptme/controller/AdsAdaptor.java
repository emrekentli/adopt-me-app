package com.emrekentli.adoptme.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.model.PostModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdsAdaptor extends ArrayAdapter<PostModel> {
    List<PostModel> listData;
    Context context;
    int resource;



    public AdsAdaptor(@NonNull Context context, int resource, @NonNull List<PostModel> listData) {
        super(context, resource, listData);
        this.context=context;
        this.resource=resource;
        this.listData=listData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null){
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView=inflater.inflate(R.layout.lastads_row,null,true);
        }
            PostModel listdata=getItem(position);
            ImageView img = (ImageView) convertView.findViewById(R.id.image_view);
            TextView txtCountry = convertView.findViewById(R.id.country);

        TextView txtName = convertView.findViewById(R.id.ads_name);
        txtName.setText(listdata.getName());

            txtCountry.setText(listdata.getCity().getName());

        String base64Image = listdata.getMainImage();
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        img.setImageBitmap(originalBitmap);
        return convertView;
    }



}
