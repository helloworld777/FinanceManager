package com.lu.momeymanager.util;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;

import com.lu.momeymanager.view.widget.dialog.DialogLoading;

public class AsyncTaskUtil {

    private IAsyncTaskCallBack iAsyncTaskCallBack;
    //	private Context context;
    private DialogLoading dialogLoading;

    public AsyncTaskUtil() {

    }

    public AsyncTaskUtil(Context context) {
//		this.context=context;
        dialogLoading = new DialogLoading(context);
    }

    public AsyncTaskUtil setIAsyncTaskCallBack(IAsyncTaskCallBack iAsyncTaskCallBack) {
        this.iAsyncTaskCallBack = iAsyncTaskCallBack;
        return this;
    }

    public void execute(String... params) {
        new MyTask().execute(params);
    }

    class MyTask extends AsyncTask<String, integer, Object> {
        @Override
        protected void onPreExecute() {
            if (dialogLoading != null && !dialogLoading.isShowing()) {
                dialogLoading.show();
            }
        }

        @Override
        protected Object doInBackground(String... arg0) {
            return iAsyncTaskCallBack.doInBackground(arg0);
        }

        @Override
        protected void onPostExecute(Object result) {
            iAsyncTaskCallBack.onPostExecute(result);
            if(dialogLoading!=null){
                dialogLoading.dismiss();
            }
        }

    }

    public interface IAsyncTaskCallBack {
        public Object doInBackground(String... arg0);

        public void onPostExecute(Object result);
    }
}
