package com.example.whatsp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<MessageModel> messageModelList;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messageModelList = new ArrayList<>();
    }

    public void add(MessageModel messageModel) {
        messageModelList.add(messageModel);
        notifyItemInserted(messageModelList.size() - 1);
    }

    public void clear() {
        messageModelList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<MessageModel> messages) {
        int startIndex = messageModelList.size();
        messageModelList.addAll(messages);
        notifyItemRangeInserted(startIndex, messages.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENT) {
            View view = inflater.inflate(R.layout.message_row_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.message_row_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(messageModel);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(messageModel);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSentMessage;

        public SentMessageViewHolder(View itemView) {
            super(itemView);
            textViewSentMessage = itemView.findViewById(R.id.textViewSentMessage);
        }

        public void bind(MessageModel messageModel) {
            textViewSentMessage.setText(messageModel.getMessage());
        }
    }

    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReceiveMessage;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            textViewReceiveMessage = itemView.findViewById(R.id.textViewReceivedMessage);
        }

        public void bind(MessageModel messageModel) {
            textViewReceiveMessage.setText(messageModel.getMessage());
        }
    }
}
