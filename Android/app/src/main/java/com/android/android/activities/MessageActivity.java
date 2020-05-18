package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.MessageListAdapter;
import com.android.android.entities.Message;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;

import java.util.List;
import java.util.UUID;

public class MessageActivity extends AppCompatActivity {

    private User user;

    private ListView messageListView;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        user = User.getUser();
        messageListView = findViewById(R.id.messageList);
        messageText = findViewById(R.id.messageText);
        List<Message> messageList = getUserMessages();
        if(messageList != null) {
            populateMessageList(messageList);
        }
    }

    private List<Message> getUserMessages() {
        UUID userId = user.getId();
        List<Message> messageList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/messages/user";
        try {
            apiCaller.execute("GET", url, user.getAuthenticationToken().toString(),
                    userId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        messageList = Message.createMessageListFromJson(apiResponse.getJson());
                        messageText.setText(getResources().getString(R.string.api_success_to_get_user_messages));
                    } catch(Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_to_get_user_messages));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
        return messageList;
    }

    private void populateMessageList(List<Message> messageList) {
        MessageListAdapter messageListAdapter = new MessageListAdapter(
                this, R.layout.message_list_view, messageList);
        messageListView.setAdapter(messageListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map:
                ActivityStarter.openMapActivity(this);
                return true;
            case R.id.menu_account:
                ActivityStarter.openAccountActivity(this);
                return true;
            case R.id.menu_payment:
                ActivityStarter.openPaymentActivity(this);
                return true;
            case R.id.menu_report_history:
                ActivityStarter.openReportHistoryActivity(this);
                return true;
            case R.id.menu_transaction_history:
                ActivityStarter.openTransactionHistoryActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
