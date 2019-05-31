package com.zeidat.getlamd.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class AppointmentsAdapter extends ArrayAdapter<String> {
    public AppointmentsAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
