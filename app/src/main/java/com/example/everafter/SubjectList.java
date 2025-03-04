package com.example.everafter;

import java.util.ArrayList;
import java.util.List;

// Ensure that the Event class is imported properly.
// For example, if it's in the same package, this is sufficient; otherwise, adjust the package name.
import com.example.everafter.Event;

public class SubjectList {
    private int id;
    private String name;
    private int user_id;
    private List<Event> events;

    public SubjectList(int id, int user_id, String name) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return user_id;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return name;
    }
}
