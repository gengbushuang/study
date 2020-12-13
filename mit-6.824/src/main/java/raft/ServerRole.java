package raft;

/**
 * raft状态
 */
public enum ServerRole {

    Follower,//追随者
    Candidate,//候选者
    Leader//领导者
}