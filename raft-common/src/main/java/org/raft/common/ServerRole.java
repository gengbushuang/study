package org.raft.common;

/**
 * 状态机制
 * 
 * 追随者
 * 	>响应来自候选者和领导者的请求
 * 	>如果在超过选举的超时时间之前没有收到领导者的心跳或者是候选者请求投票，自己变为候选者
 * 
 * 候选者
 *	>在转变为候选者后立即开始选举过程
 *		>>自增当前的任期号(currentTerm)
 *		>>给自己投票
 *		>>重置选举超时计时器
 *		>>发送请求的投票的rpc给其他所有服务器
 *	>如果接受到大多数服务器的选票，那么就变成领导者
 *	>如果接受到来自领导人的附加日志RPC，转变为跟随者
 *	>如果选举过程超时,再次发起一轮选举
 *
 * 领导者
 * 	>一旦成为领导人,发送空的附加日志RPC(心跳)给其他所有的服务器,在一定空余时间不停的重复发送,以阻止跟随者超时
 * 	>如果接收到来自客户端的请求，附加条目到本地日志中，在条目被应用到状态机后响应客户端
 *	>如果对于一个跟随者，最后日志条目的索引值大于等于 nextIndex，那么：发送从 nextIndex 开始的所有日志条目
 *		>>如果成功：更新相应跟随者的 nextIndex 和 matchIndex
 *		>>如果因为日志不一致而失败，减少 nextIndex 重试
 *	>如果存在一个满足N > commitIndex的 N，并且大多数的matchIndex[i] ≥ N成立，并且log[N].term == currentTerm成立，那么令 commitIndex 等于这个 N
 * 		
 * 
 * 
 * 初始化的时候为Follower，超时到转化为Candidate，接收到领导信息转化为Follower如果接受到同意选票转化为Leader，
 * 
 * @author gbs
 *
 */
public enum ServerRole {

	Follower("追随者"), Candidate("候选者"), Leader("领导者");

	private final String desc;

	private ServerRole(String desc) {
		this.desc = desc;
	}
}