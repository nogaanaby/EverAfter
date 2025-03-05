package com.example.everafter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ItemsListAdapter extends ArrayAdapter<Item> {

    private ItemsListActivity.ActionListener actionListener;

    public ItemsListAdapter(Context context, List<Item> items, ItemsListActivity.ActionListener listener) {
        super(context, 0, items);
        this.actionListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_row, parent, false);
        }
        TextView tvItemDisplay = convertView.findViewById(R.id.tvItemDisplay);
        ImageButton btnAddSubItem = convertView.findViewById(R.id.btnAddSubItem);
        ImageButton btnEditItem = convertView.findViewById(R.id.btnEditItem);
        ImageButton btnDeleteItem = convertView.findViewById(R.id.btnDeleteItem);

        tvItemDisplay.setText(item.getDisplayText());

        btnAddSubItem.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAddSubItem(item);
            }
        });

        btnEditItem.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditItem(item);
            }
        });

        btnDeleteItem.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (actionListener != null) {
                            actionListener.onDeleteItem(item);
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return convertView;
    }
}
