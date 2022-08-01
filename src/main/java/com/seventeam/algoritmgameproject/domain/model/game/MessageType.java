package com.seventeam.algoritmgameproject.domain.model.game;

public enum MessageType {
    COMPILE_FAIL_LOSE("컴파일 3회 실패"),
    TIMEOUT("시간 초과"),
    EXIT_LOSE("탈주"),
    WIN("승리"),
    FAIL("상대 컴파일 실패"),
    LOSE("패배"),
    EXIT("상대방이 나갔습니다.");


    private final String state;

    MessageType(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
