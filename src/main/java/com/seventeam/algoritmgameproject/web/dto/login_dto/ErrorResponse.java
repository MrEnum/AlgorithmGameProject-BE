package com.seventeam.algoritmgameproject.web.dto.login_dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 6494678977033306639L;
    private boolean reLogin;
    private String error;

    public ErrorResponse(String error) {
        this.reLogin = true;
        this.error = error;
    }
}
