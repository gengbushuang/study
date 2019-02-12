package raft;

/**
 * raft状态
 */
public enum ServerRole {

    Follower,
    Candidate,
    Leader
}