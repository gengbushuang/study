package mapreduce.common;

public enum JobPhase {
    MAP_PHASE("mapPhase"),
    REDUCE_PHASE("reducePhase");

    private final String name;

    JobPhase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
