package raft.t;

import java.util.*;
import java.util.stream.Collectors;

public class NodeGroup {
    private final int selfId;
    private Map<Integer, GroupMember> memberMap;

    NodeGroup(NodeEndpoint endpoint) {
        this(Collections.singleton(endpoint), endpoint.getId());
    }

    NodeGroup(Collection<NodeEndpoint> endpoints, int selfId) {
        this.memberMap = buildMemberMap(endpoints);
        this.selfId = selfId;
    }

    private Map<Integer, GroupMember> buildMemberMap(Collection<NodeEndpoint> endpoints) {
        Map<Integer, GroupMember> map = new HashMap<>();
        for (NodeEndpoint endpoint : endpoints) {
            map.put(endpoint.getId(), new GroupMember(endpoint));
        }

        return map;
    }

    GroupMember findMember(Integer id) {
        GroupMember member = getMember(id);
        if (member == null) {
            throw new IllegalArgumentException("no such node " + id);
        }
        return member;
    }

    GroupMember getMember(Integer id) {
        return memberMap.get(id);
    }

    Collection<GroupMember> listReplicationTarget() {
        return memberMap.values().stream().filter(m -> !m.idEquals(selfId)).collect(Collectors.toList());
    }

    Set<NodeEndpoint> listEndpointExceptSelf() {
        Set<NodeEndpoint> endpoints = new HashSet<>();
        for (GroupMember member : memberMap.values()) {
            if (!member.idEquals(selfId)) {
                endpoints.add(member.getEndpoint());
            }
        }
        return endpoints;
    }
}
