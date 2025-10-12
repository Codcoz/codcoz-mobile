package com.sustria.codcoz.ui.cardapio.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.sustria.codcoz.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listGroup;
    private HashMap<String, List<String>> listChild;

    public ExpandableListAdapter(Context context, List<String> listGroup,
                                 HashMap<String, List<String>> listChild) {
        this.context = context;
        this.listGroup = listGroup;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.group_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.groupTitle);
        textView.setText(groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.child_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.childTitle);
        textView.setText(childTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
