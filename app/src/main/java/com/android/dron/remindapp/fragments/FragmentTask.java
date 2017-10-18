package com.android.dron.remindapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.dron.remindapp.DialogFragmentTask;
import com.android.dron.remindapp.R;
import com.android.dron.remindapp.adapters.FragmentTaskRecViewAdapter;
import com.android.dron.remindapp.content.ContentTaskActivity;
import com.android.dron.remindapp.db.DB;
import com.android.dron.remindapp.model.Note;
import com.android.dron.remindapp.util.RequestCode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentTask extends Fragment {

    public List<Note> list;
    public FragmentTaskRecViewAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView imageView;
    public static final String TAG_TASK_CONTENT_TEXT = "content";
    public static final String TAG_TASK_ID = "position";
    public Cursor cursor;
    public DB db;
    private final String TAG = "Методы фрагмента: ";
    private String st;
    private String idDel;
    private String idUpd;
    private String changeText;
    private String changeTime;
    private ProgressBar progressBar;
    private Paint p = new Paint();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        list = new ArrayList<>();
        db = new DB(getContext());
        db.open();
        // cursor = db.query("NOTE", new String[]{"NOTE_ITEM", "TIME_DATE", "id"}, null, null, null, null, null);
//            ArrayList<WhateverTypeYouWant> mArrayList = new ArrayList<WhateverTypeYouWant>();
//            for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
//                mArrayList.add(mCursor.getWhateverTypeYouWant(WHATEVER_COLUMN_INDEX_YOU_WANT));
//            }
        cursor = db.getAllDataNote();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(new Note(cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
        } else {
            Toast.makeText(getContext(), "Список пуст!", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        final View inflate = inflater.inflate(R.layout.fragment_task, container, false);
        imageView = (ImageView) inflate.findViewById(R.id.iv_fragment_1);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view_task);
        progressBar = (ProgressBar) inflate.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        adapter = new FragmentTaskRecViewAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sizeList();
        adapter.notifyDataSetChanged();
        initSwipe();
        return inflate;
    }

    public void openDialogFragmentTask() {
        DialogFragment dialogFragmentTask = new DialogFragmentTask();
        dialogFragmentTask.setTargetFragment(this, RequestCode.Task.REQUEST_CODE_FAB_OK);
        dialogFragmentTask.show(getActivity().getSupportFragmentManager(), "1");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RequestCode.Task.REQUEST_CODE_FAB_OK:
                    st = data.getStringExtra(DialogFragmentTask.TAG_TEXT_NOTES);
                    new MyAsyncTask().execute("add");
                    break;

                case RequestCode.Task.REQUEST_CODE_FAB_CANCEL:
                    sizeList();
                    Toast.makeText(getContext(), "Cancel", Toast.LENGTH_LONG).show();
                    break;

                case RequestCode.Task.REQUEST_CODE_TASK_EDIT_TEXT:
                    changeText = data.getStringExtra("edit");
                    changeTime = data.getStringExtra("date");
                    idUpd = data.getStringExtra("pos");
                    new MyAsyncTask().execute("upd");
                    break;
            }
        } else {
            sizeList();
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Clicking Back", Toast.LENGTH_LONG).show();
        }
    }

    public void createAlertDialogRemove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.name_delete_dialog)
                .setMessage(R.string.message_delete_dialog)
                .setIcon(R.drawable.delete_forever)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new MyAsyncTask().execute("del");
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show();
    }

    public void sizeList() {
        if (list.size() == 0)
            imageView.setVisibility(View.VISIBLE);
        else {
            imageView.setVisibility(View.GONE);
        }
    }

    public String setListDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date(System.currentTimeMillis())) + "\n" + timeFormat.format(new Date(System.currentTimeMillis()));
        return date;
    }

    // swipe
    public void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            Intent intent;

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    idDel = list.get(position).getDate_time();
                    createAlertDialogRemove();
                } else {
                    String content_text = list.get(position).getNote();
                    intent = new Intent(getContext(), ContentTaskActivity.class);
                    intent.putExtra(TAG_TASK_CONTENT_TEXT, content_text);
                    intent.putExtra(TAG_TASK_ID, list.get(position).getDate_time());
                    startActivityForResult(intent, RequestCode.Task.REQUEST_CODE_TASK_EDIT_TEXT);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                Drawable drawable;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        drawable = getResources().getDrawable(R.drawable.pencil);
                        icon = drawableToBitmap(drawable);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.pencil);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        drawable = getResources().getDrawable(R.drawable.close);
                        icon = drawableToBitmap(drawable);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete_forever);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        sizeList();
        db.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        cursor.close();
        db.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        list.clear();
        cursor.close();
        db.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }

    // Convert a Drawable to Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    private class MyAsyncTask extends AsyncTask<String, Void, Integer> {
        private DB db;
        final String ADD = "add";
        final String DEL = "del";
        final String UPD = "upd";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String s = params[0];
            db = new DB(getContext());
            db.open();
            switch (s) {
                case ADD:
                    db.addRecNote(st, setListDateFormat());
                    refreshData();
                    publishProgress();
//                    SystemClock.sleep(2000);
                    return 1;
                case DEL:
                    db.delRecNote(idDel);
                    refreshData();
                    publishProgress();
                    return 2;
                case UPD:
                    db.upRecNote(changeText, changeTime, idUpd);
                    refreshData();
                    publishProgress();
                    return 3;
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer aBoolean) {
            super.onPostExecute(aBoolean);
            switch (aBoolean) {
                case 1:
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    progressBar.setVisibility(View.GONE);
                    sizeList();
                    Toast.makeText(getContext(), "Add", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    sizeList();
                    Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                    break;

                case 3:
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Update", Toast.LENGTH_LONG).show();

                    break;

                default:
                    Toast.makeText(getContext(), "Without changes", Toast.LENGTH_LONG).show();
            }
        }

        void refreshData() {
            if (list == null) {
                list = new ArrayList<>();
            } else {
                list.clear();
            }
            cursor = db.getAllDataNote();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Note(cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            cursor.close();
        }
    }
}


