package com.example.tswalano.edutrack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tswalano.edutrack.model.DataModel;

import java.util.ArrayList;

/**
 * Created by Tswalano on 2018/07/21.
 */

public class StudentAdapter extends ArrayAdapter<DataModel> {

    private int listItemLayout;

    public StudentAdapter(Context context, int layoutId, ArrayList<DataModel> dataModels) {
        super(context, layoutId, dataModels);
        listItemLayout = layoutId;
    }

//    public StudentAdapter(ArrayList<DataModel> data, Context context) {
//        super(context, R.layout.list_items, data);
//        this.dataSet = data;
//        this.mContext=context;
//
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);

            viewHolder.txtStudentMark = convertView.findViewById(R.id.txtStudentMark);
            viewHolder.txtStudentSubject = convertView.findViewById(R.id.txtStudentSubject);
            viewHolder.progressBar = convertView.findViewById(R.id.progressBar);

            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.txtStudentMark.setText(Integer.toString(dataModel.getMark()));
        viewHolder.txtStudentSubject.setText(dataModel.getSubject());
        viewHolder.progressBar.setProgress(dataModel.getMark());

        // Return the completed view to render on screen
        return convertView;
    }

    // The ViewHolder, only one item for simplicity and demonstration purposes, you can put all the views inside a row of the list into this ViewHolder
    private static class ViewHolder {
        TextView txtStudentSubject;
        TextView txtStudentMark;
        ProgressBar progressBar;
    }
}