package com.threadtest.promise.js;

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