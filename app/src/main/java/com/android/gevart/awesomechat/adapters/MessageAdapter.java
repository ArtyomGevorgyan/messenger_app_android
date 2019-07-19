package com.android.gevart.awesomechat.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.gevart.awesomechat.R;
import com.android.gevart.awesomechat.models.Message;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private static final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public MessageAdapter(@NonNull Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        TextView textTextView = convertView.findViewById(R.id.textTextView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        Message message = getItem(position);
        boolean isText = message.getImageUrl() == null;
        if (isText) {
            textTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            textTextView.setText(message.getText());
        } else {
            textTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getImageUrl())
                    .into(photoImageView);
        }
        nameTextView.setText(message.getSenderUserName());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;

        if (message.getSenderUserId().equals(currentUserID)) {
            textTextView.setLayoutParams(params);
            photoImageView.setLayoutParams(params);
            nameTextView.setLayoutParams(params);
        }
        return convertView;
    }
}
