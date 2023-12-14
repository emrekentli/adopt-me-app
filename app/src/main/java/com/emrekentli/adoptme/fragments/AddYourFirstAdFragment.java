package com.emrekentli.adoptme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;

public class AddYourFirstAdFragment extends Fragment {
  TextView ilanekle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bu fragment'in layout'unu hazır hale getirelim

        View view = inflater.inflate(R.layout.addfirstads, container, false);


        ilanekle = view.findViewById(R.id.ilanekle);

        ilanekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragments(NewAdd_Fragment.class);

            }
        });

        return view;

    }


    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }

}
