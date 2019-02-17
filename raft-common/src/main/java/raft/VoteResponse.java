package raft;

public class VoteResponse {
	private long term;//当前任期号，已便于候选人去更新自己的任期号
	private boolean granted;//候选人赢得了此张选票时为真
}
