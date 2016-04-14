package com.example.android_robot_01;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_robot_01.bean.ChatMessage;
import com.example.android_robot_01.bean.ChatMessage.Type;
import com.example.android_robot_01.bean.Result;
import com.lu.financemanager.R;
import com.lu.momeymanager.util.Debug;
import com.lu.momeymanager.util.DialogUtil;
import com.lu.momeymanager.util.TopNoticeDialog;

import java.util.List;

/**
 * App ID: 7991891
 * <p/>
 * API Key: NEMOr9iVcjPBaA1G3GLypcca
 * <p/>
 * Secret Key: 4ad1a0bf27ac6f3ef4ef09f77cf1ec78
 */
public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;
//    DialogUtil dialogUtil;
    public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 接受到消息为1，发送消息为0
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = mDatas.get(position);
        return msg.getType() == Type.INPUT ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final  ChatMessage chatMessage = mDatas.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (chatMessage.getType() == Type.INPUT) {
                convertView = mInflater.inflate(R.layout.main_chat_from_msg,
                        parent, false);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_from_content);
//				viewHolder.listview= (ListView) convertView.findViewById(R.id.listview);
                convertView.setTag(viewHolder);
            } else {
                convertView = mInflater.inflate(R.layout.main_chat_send_msg,
                        null);

                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_send_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_send_content);
//				viewHolder.listview= (ListView) convertView.findViewById(R.id.listview);
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(chatMessage.getMsg());
        viewHolder.createDate.setText(chatMessage.getDateStr());
//		ArrayAdapter<String>

        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Debug.d(ChatMessageAdapter.class,"onClick.......");
                lookDetailInfo(position,parent.getContext());

            }
        });
        viewHolder.content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Debug.d(ChatMessageAdapter.class,"onLongClick.......");
                showEditDialog(parent.getContext(),chatMessage.getMsg());
                return true;
            }
        });
        return convertView;
    }
    private void showEditDialog(final Context context,final String data){
        String[] items={"复制"};
        ClipboardManager myClipboard= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        DialogUtil.showAlertDialog(context, "选择", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity)context).copeData(data);
                TopNoticeDialog.showToast(context,"已复制到剪切板");
                DialogUtil.closeAlertDialog();
            }
        });
    }
    private void lookDetailInfo(int position,Context context){
        ChatMessage message = mDatas.get(position);
        if (message.result != null && !TextUtils.isEmpty(message.result.url)) {
            Intent intent = new Intent(context, ShowHtmlActivity.class);
            intent.putExtra(ShowHtmlActivity.TEXT, message.getMsg());
            intent.putExtra(ShowHtmlActivity.URL, message.result.url);
            context.startActivity(intent);
        } else if (message.result != null && (message.result.getCode() == Result.TYPE_NEW || message.result.getCode() == Result.TYPE_RECIPE)) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.RESULT, message.result);
            context.startActivity(intent);
        }
    }
    class MyOnclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }
    private class ViewHolder {
        public TextView createDate;
        public TextView name;
        public TextView content;
        public ListView listview;
    }

}
