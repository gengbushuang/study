package raft.t;

/**
 * raft状态
 */
public enum ServerRole {

    Follower,//跟随者
    Candidate,//候选人
    Leader//领导人
}