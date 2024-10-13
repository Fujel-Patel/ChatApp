package com.example.whatsp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

  private String receiverId, receiverName, senderRoom, receiverRoom;
  private DatabaseReference dbReferencesender, dbReferenceReceiver, userReference;
  private ImageView sendBtn;
  private EditText messageText;
  private RecyclerView recyclerView;
  private MessageAdapter messageAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    userReference = FirebaseDatabase.getInstance().getReference("users");

    receiverId = getIntent().getStringExtra("id");
    receiverName = getIntent().getStringExtra("name");

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(receiverName);
    }

    if (receiverId != null && FirebaseAuth.getInstance().getUid() != null) {
      senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
      receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();
    }

    sendBtn = findViewById(R.id.sendmassageIcon);
    messageAdapter = new MessageAdapter(this);
    recyclerView = findViewById(R.id.chatrecycler);
    messageText = findViewById(R.id.massageEdit);

    recyclerView.setAdapter(messageAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    dbReferencesender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
    dbReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

    loadMessages();

    sendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String message = messageText.getText().toString().trim();
        if (!message.isEmpty()) {
          sendMessage(message);
        } else {
          Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void loadMessages() {
    dbReferencesender.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        List<MessageModel> messages = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
          MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
          messages.add(messageModel);
        }
        messageAdapter.clear();
        messageAdapter.addAll(messages);
        messageAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        // Handle error
      }
    });
  }

  private void sendMessage(String message) {
    String messageId = UUID.randomUUID().toString();
    long timestamp = System.currentTimeMillis();
    MessageModel messageModel = new MessageModel(messageId, message, FirebaseAuth.getInstance().getUid(), timestamp);

    dbReferencesender.child(messageId).setValue(messageModel)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                // Handle success if needed
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
              }
            });

    dbReferenceReceiver.child(messageId).setValue(messageModel);
    messageAdapter.add(messageModel);
    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    messageText.setText("");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.logout) {
      FirebaseAuth.getInstance().signOut();
      startActivity(new Intent(ChatActivity.this, SignInActivity.class));
      finish();
      return true;
    }
    return false;
  }
}
