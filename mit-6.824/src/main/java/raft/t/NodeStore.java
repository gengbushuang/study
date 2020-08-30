package raft.t;

public interface NodeStore {
    int getTrem();
    void setTerm(int term);
    int getVotedFor();
    void setVotedFor(int votedFor);

    void close();
}
