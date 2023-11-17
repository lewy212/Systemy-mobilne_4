package com.example.zadanie_3;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Calendar;
public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private final List<Task> tasks;
    public static TaskStorage getInstance(){return taskStorage;};

    public List<Task> getTasks() {
        return tasks;
    }

    private  TaskStorage(){
        tasks = new ArrayList<>();
        for (int i =1; i<=20;i++)
        {
            Task task = new Task();
            task.setName("Zadanie numer "+i);
            if(i%3==0)
            {
                task.setCategory(Category.STUDIES);
            }
            else {
                task.setCategory(Category.HOME);
            }
            if(task.getCategory().equals(Category.HOME)){

            }
            task.setDone(i%3 == 0);
            //task.setDate(ustawDate(i));
            tasks.add(task);
        }
    }

    private Date ustawDate(int liczba)
    {
        Calendar date = Calendar.getInstance();
        long timeInSecs=date.getTimeInMillis();
        Date data = new Date(timeInSecs + (liczba * 60 * 1000));
        return data;
    }
    public void addTask(Task task)
    {
        tasks.add(task);
    }

    public Task getTask(UUID id) {
        for (Task task : tasks) {
            if (task.getId().equals(id))
                return task;
        }
        return null;
    }
}
