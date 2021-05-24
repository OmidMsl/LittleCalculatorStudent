package com.omidmsl.multiplyanddivisiononline.ui.conversation;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    Context context;
    private List<Message> messages;
    private int numOfDays;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemCount() {
        return messages.size() - numOfDays;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.display(position);
    }

    public void updateData(List<Message> messages) {
        numOfDays = 0;
        if (!messages.isEmpty()) {
            Message m = new Message();
            m.setId(-3);
            m.setDate(messages.get(0).getDate());
            messages.add(0, m);
            numOfDays++;
        }
        for (int i = 0; i < messages.size() - 1; i++) {
            if (!messages.get(i).getDate().getPersianShortDate().equals(messages.get(i + 1).getDate().getPersianShortDate())) {
                Message m = new Message();
                m.setId(-3);
                m.setDate(messages.get(i).getDate());
                messages.add(i + 1, m);
                numOfDays++;
                i++;
            }
        }
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void sendMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public List<Message> getNotSentMessages(){
        List<Message> ms = new ArrayList<>();
        for (Message m : messages){
            if (m.getId()==-2)
                ms.add(m);
        }
        return ms;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout sendLayout;
        private final TextView sendContent;
        private final TextView sendTime;
        private final ImageView status;
        private final LinearLayout receiveLayout;
        private final TextView receiveContent;
        private final TextView receiveTime;
        private final TextView date;


        MyViewHolder(final View itemView) {
            super(itemView);

            sendLayout = itemView.findViewById(R.id.lm_send);
            sendContent = itemView.findViewById(R.id.lm_send_content);
            sendTime = itemView.findViewById(R.id.lm_send_textView_time);
            status = itemView.findViewById(R.id.lm_send_imageView_status);
            receiveLayout = itemView.findViewById(R.id.lm_receive);
            receiveContent = itemView.findViewById(R.id.lm_receive_content);
            receiveTime = itemView.findViewById(R.id.lm_receive_textView_time);
            date = itemView.findViewById(R.id.lm_textView_date);

        }


        void display(int position) {
            Message message = messages.get(position);
            if (message.getId() == -3) {
                sendLayout.setVisibility(View.GONE);
                receiveLayout.setVisibility(View.GONE);
                date.setText(message.getDate().getPersianLongDate());
            } else if (message.isFromTeacher()) {
                sendLayout.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                receiveContent.setText(message.getContent());
                receiveTime.setText(message.getDate().getTime().getHours()
                        + ":" + message.getDate().getTime().getMinutes());
            } else {
                receiveLayout.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                sendContent.setText(message.getContent());
                sendTime.setText(message.getDate().getTime().getHours()
                        + ":" + message.getDate().getTime().getMinutes());
                status.setImageResource(message.getId() == -2 ? R.drawable.ic_schedule_24px : message.isObserved() ?
                        R.drawable.ic_done_all_black_24dp : R.drawable.ic_done_black_24dp);
            }
            if (message.isFromTeacher() && !message.isObserved()) {
                Connection.RequestPackage rp = new Connection.RequestPackage();
                rp.setMethod("POST");
                rp.setFileName("observeMessage.php");
                rp.setParameter("id", String.valueOf(message.getId()));
                new setObservedTask().execute(rp);
            }
        }
    }

    private class setObservedTask extends AsyncTask<Connection.RequestPackage, Void, String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error")) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
