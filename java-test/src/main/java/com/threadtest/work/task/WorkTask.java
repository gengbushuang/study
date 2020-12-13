package com.threadtest.work.task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkTask {

    private TaskNode root;

    private List<TaskEdge> edges;

    private TaskNode end;

    private AtomicBoolean done = new AtomicBoolean(false);

    public WorkTask() {
        edges = new LinkedList<>();
        root = new TaskNode(null);
        end = new TaskNode(new EndTask());
    }

    public void start(TaskContext o) {
        root.executeTask(o);
    }

    public void goStart(TaskContext o) {
        root.goExecuteTask(o);
    }

    public void addStartNode(TaskNode t) {
        edges.add(addEdge0(root, t));
    }

    public void addEdge(TaskNode prev, TaskNode next) {
        edges.add(addEdge0(prev, next));
    }

    public void connectToEnd(TaskNode t) {
        edges.add(addEdge0(t, end));
    }

    public void waitDone() throws InterruptedException {
        synchronized (done) {
            while (!done.get()) {
                done.wait();
            }
        }
    }

    private TaskEdge addEdge0(TaskNode pre, TaskNode next) {
        TaskEdge edge = new TaskEdge(pre, next);
        pre.addChildren(edge);
        next.addDependency(edge);
        return edge;
    }

    private class EndTask implements Task {
        @Override
        public void run(TaskContext context) {
            synchronized (done) {
                done.set(true);
                done.notify();
            }
        }
    }
}
