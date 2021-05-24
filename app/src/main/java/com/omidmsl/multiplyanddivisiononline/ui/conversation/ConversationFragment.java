package com.omidmsl.multiplyanddivisiononline.ui.conversation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.models.Message;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;

public class ConversationFragment extends Fragment {

    MessageAdapter messageAdapter;
    int studentId, teacherId;
    Context context;
    Progress progress;
    boolean firstEnter, isNetOn, receiveing;
    ImageView sendButton;
    EditText content;
    RecyclerView rv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversation, container, false);

        rv = root.findViewById(R.id.rv_conversation);
        sendButton = root.findViewById(R.id.fc_imageView_send);
        content = root.findViewById(R.id.fc_editText);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        context = getActivity();
        progress = Progress.getInstance();

        receiveing = false;
        SharedPreferences sp = context.getSharedPreferences("studentInfo", MODE_PRIVATE);
        studentId = sp.getInt(Student.KEY_ID, -2);
        teacherId = sp.getInt(Teacher.KEY_ID, -2);

        messageAdapter = new MessageAdapter(context, new ArrayList<Message>());
        rv.setAdapter(messageAdapter);
        firstEnter = true;
        isNetOn = true;

        Timer timer = new Timer();
        TimerTask task = new checkForNew();
        timer.schedule(task, 0, 10000);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message m = new Message();
                m.setObserved(false);
                m.setFromTeacher(false);
                m.setReceiverId(teacherId);
                m.setSenderId(studentId);
                m.setDate(new PersianCalendar(new Date().getTime()));
                m.setContent(content.getText().toString().trim());
                content.setText("");
                m.setId(-2);
                messageAdapter.sendMessage(m);

                Connection.RequestPackage rp = new Connection.RequestPackage();
                rp.setFileName("sendMessage.php");
                rp.setMethod("POST");
                rp.setParameter("content", m.getContent());
                rp.setParameter("sender_id", String.valueOf(m.getSenderId()));
                rp.setParameter("receiver_id", String.valueOf(m.getReceiverId()));
                rp.setParameter("is_from_teacher", "0");
                rp.setParameter("is_observed", "0");
                rp.setParameter("date", String.valueOf(m.getDate().getTimeInMillis()));
                new getMessagesTask().execute(rp);

            }
        });

        return root;
    }


    private void loadMessages(ArrayList<Message> messages) {
        messageAdapter.updateData(messages);
        scrollDown();
    }

    private void scrollDown(){
        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.scrollToPosition(messageAdapter.getItemCount() - 1);
                // Here adapter.getItemCount()== child count
            }
        });
    }

    private class getMessagesTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            receiveing = true;
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s==null)
                return;
            if (!s.contains("Error")) {
                loadMessages((ArrayList<Message>) JsonParser.parseMessage(s));
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            receiveing = false;
            progress.hideProgress();
        }
    }

    private class checkForNew extends TimerTask {
        public void run() {
            if (receiveing)
                return;
            if (isNetOn){
                if (!Connection.isOnline(context)) {
                    if (firstEnter)
                        new OfflineAlert(context);
                    else Snackbar.make(getView(), "اینترنت شما متصل نیست!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    isNetOn = false;
                } else {
                    if (firstEnter){
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                progress.showProgress(context, "در حال دریافت پیام ها ...", false);
                            }
                        });
                        Connection.RequestPackage rp = new Connection.RequestPackage();
                        rp.setFileName("getMessages.php");
                        rp.setMethod("POST");
                        rp.setParameter("id", String.valueOf(studentId));
                        new getMessagesTask().execute(rp);
                        firstEnter = false;
                    } else {
                        Connection.RequestPackage rp = new Connection.RequestPackage();
                        rp.setFileName("getMessages.php");
                        rp.setMethod("POST");
                        rp.setParameter("id", String.valueOf(studentId));
                        new getMessagesTask().execute(rp);
                    }
                }
            } else if (Connection.isOnline(context))
                isNetOn = true;
        }
    }
}
