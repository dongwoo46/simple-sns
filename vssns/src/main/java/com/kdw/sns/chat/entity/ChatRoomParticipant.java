package com.kdw.sns.chat.entity;

import com.kdw.sns.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "chat_room_participant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomParticipant {

    @EmbeddedId
    private ChatRoomParticipantId id;

    @MapsId("participantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Member participant;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;

    @Column(name = "exited_at")
    private LocalDate exitedAt;
}
