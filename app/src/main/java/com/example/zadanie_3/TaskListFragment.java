package com.example.zadanie_3;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private boolean subtitleVisible;
    public static final String KEY_EXTRA_TASK_ID = "extra_task_id";
    private static final String KEY_CURRENT_BOOLEAN ="current_boolean";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_task_list,container,false);
       recyclerView  = view.findViewById(R.id.task_recycler_view);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       setHasOptionsMenu(true);
       if(savedInstanceState!=null)
       {
           subtitleVisible = savedInstanceState.getBoolean(KEY_CURRENT_BOOLEAN);
       }
       return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CURRENT_BOOLEAN,subtitleVisible);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_task_menu,menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_task:
                Task task = new Task();

                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID,task.getId());
                startActivity(intent);

                return true;
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int todoTasksCount = 0;
        for(Task task : tasks){
            if(!task.isDone()){
                todoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format,todoTasksCount);
        if(!subtitleVisible){
            subtitle=null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        if (adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView, dateTextView;
        private ImageView iconImageView;
        private ImageView checkbox_wyswietl;
        private Task task;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);
            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            iconImageView = itemView.findViewById(R.id.imageView);
            checkbox_wyswietl = itemView.findViewById(R.id.imageViewCheck);

        }

        public void bind(Task task) {
            this.task = task;
            String truncatedDescription = dodajkropki(task.getName(), 18); // 30 to przykładowa maksymalna długość
            nameTextView.setText(truncatedDescription);
            dateTextView.setText(task.getDate().toString());
            if(task.getCategory().equals(Category.HOME)){
                iconImageView.setImageResource(R.drawable.ic_home);
            }else {
                iconImageView.setImageResource(R.drawable.ic_study);
            }
           if(task.isDone() == true){
                checkbox_wyswietl.setImageResource(R.drawable.tc_checked);
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
               checkbox_wyswietl.setImageResource(R.drawable.tc_not_check);
               nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
           }
        }
        private String dodajkropki(String text, int maxLength) {
            if (text.length() > maxLength) {
                return text.substring(0, maxLength - 3) + "...";
            } else {
                return text;
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }
    }
}

