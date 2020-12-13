package com.threadtest.work.task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskNode {

    private List<TaskEdge> dependency = new LinkedList<>();

    private final Task task;

    private List<TaskEdge> children = new LinkedList<>();

    private AtomicInteger atomic = new AtomicInteger(0);


    public TaskNode(Task task) {
        this.task = task;
    }

    public void addDependency(TaskEdge edge) {
        dependency.add(edge);
    }

    public void addChildren(TaskEdge edge) {
        children.add(edge);
    }

    public void executeTask(TaskContext context) {
        if (dependency.isEmpty() || dependency.size() == 1 ||
                atomic.addAndGet(1) == dependency.size()) {
            if (task != null) {
                task.run(context);
            }
            if (children.size() >= 1) {
                for (int i = 1; i < children.size(); i++) {
                    children.get(i).getNext().executeTask(context);
                }
                children.get(0).getNext().executeTask(context);
            }
        }
    }

    public void goExecuteTask(TaskContext context) {
        if (dependency.isEmpty() || dependency.size() == 1 ||
                atomic.addAndGet(1) == dependency.size()) {
            if (task != null) {
                task.run(context);
            }

            for (TaskEdge edge : children) {
                edge.getNext().goExecuteTask(context);
            }
        }
    }
}
