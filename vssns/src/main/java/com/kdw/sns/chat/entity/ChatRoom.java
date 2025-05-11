package com.kdw.sns.chat.entity;

import com.kdw.sns.common.entity.BaseEntity;
import com.kdw.sns.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name", length = 100, nullable = false)
    private String name;

    @Column(name = "is_group", nullable = false)
    private boolean isGroup;  // true: 그룹, false: 1:1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomParticipant> participants = new ArrayList<>();
}

