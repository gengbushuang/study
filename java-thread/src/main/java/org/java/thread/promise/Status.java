package org.java.thread.promise;

public enum Status {
    STATE_PENDING("pending"),
    STATE_FULFILLED("fulfilled "),
    STATE_REJECTED("rejected");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
