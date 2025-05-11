package com.kdw.sns.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatRoomParticipantId implements Serializable {

    @Column(name = "participant_id")
    private Long participantId;

    @Column(name = "room_id")
    private Long roomId;
}
