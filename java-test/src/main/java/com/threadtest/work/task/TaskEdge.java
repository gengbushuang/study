package com.threadtest.work.task;

public class TaskEdge {

    private final TaskNode prev;
    private final TaskNode next;

    public TaskEdge(TaskNode prev, TaskNode next) {
        this.prev = prev;
        this.next = next;
    }

    public TaskNode getPrev() {
        return prev;
    }

    public TaskNode getNext() {
        return next;
    }
}
