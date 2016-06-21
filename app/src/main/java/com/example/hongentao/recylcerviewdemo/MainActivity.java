package com.example.hongentao.recylcerviewdemo;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout container;
    private RecyclerView recyclerView;
    private EditText editText;
    private LinearLayoutManager mLayoutManager;
    private List<String> mData;
    private FloatingActionButton fab;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // v1.0 to 1.0.1
        init();
        initView();
        initListener();
        initAdapter();
    }

    private void init() {
        mData = new ArrayList<>();
        for (int a = 0; a < 50; a++) {
            mData.add(a + "");
        }
    }

    private void initView() {
        container = (CoordinatorLayout) findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        editText = (EditText) findViewById(R.id.edittext);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        SlideInOutLeftItemAnimator animator = new SlideInOutLeftItemAnimator(recyclerView);
        recyclerView.setItemAnimator(animator);
        mAdapter = new MyAdapter();
        mAdapter.list.clear();
        mAdapter.list.addAll(mData);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData != null) {
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        mAdapter.add(editText.getText().toString(), mAdapter.getItemCount());
                        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        editText.setText("");
                    } else {
                        Snackbar.make(container, "content can not be null!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float startY;
            private float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        endY = event.getRawY();
                        float data = Math.abs(endY - startY);
                        if (data > ViewConfiguration.get(MainActivity.this).getScaledTouchSlop()) {
                            if (editText != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            }
                        }
                        break;
                    default:
                        break;

                }
                return false;
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        public List<String> list = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder != null) {
                holder.textview.setText(list.get(position));
            }
        }

        public void add(String text, int position) {
            list.add(position, text);
            notifyItemInserted(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textview;

            public ViewHolder(View view) {
                super(view);
                textview = (TextView) view.findViewById(R.id.item_textview);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
