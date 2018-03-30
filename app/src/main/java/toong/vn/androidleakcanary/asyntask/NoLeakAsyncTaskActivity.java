package toong.vn.androidleakcanary.asyntask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import toong.vn.androidleakcanary.R;
import toong.vn.androidleakcanary.SecondActivity;

public class NoLeakAsyncTaskActivity extends AppCompatActivity {
    private TextView tvData;
    private ExampleAsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        findViewById(R.id.btnStartActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(NoLeakAsyncTaskActivity.this, SecondActivity.class));
            }
        });

        asyncTask = new ExampleAsyncTask();
        asyncTask.setListener(new ExampleAsyncTaskListener() {
            @Override
            public void onExampleAsyncTaskFinished(Integer value) {
                tvData.setText("abc");
            }
        });
        asyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        asyncTask.setListener(null);
        super.onDestroy();
    }

    static class ExampleAsyncTask extends AsyncTask<Void, Void, Integer> {
        private ExampleAsyncTaskListener listener;
        String TAG = getClass().getSimpleName();

        @Override
        protected Integer doInBackground(Void... voids) {
            int i = 0;
            while (i < 120) {
                Log.i(TAG, "" + Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer value) {
            Log.i(TAG, "" + Thread.currentThread().getName() + " onPostExecute");
            super.onPostExecute(value);
            if (listener != null) {
                listener.onExampleAsyncTaskFinished(value);
            }
        }

        public void setListener(ExampleAsyncTaskListener listener) {
            this.listener = listener;
        }
    }

    public interface ExampleAsyncTaskListener {
        void onExampleAsyncTaskFinished(Integer value);
    }
}
