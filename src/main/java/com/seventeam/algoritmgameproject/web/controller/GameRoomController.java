package com.seventeam.algoritmgameproject.web.controller;

import com.seventeam.algoritmgameproject.config.HttpCode;
import com.seventeam.algoritmgameproject.web.service.login_service.UserDetailImpl;
import com.seventeam.algoritmgameproject.web.dto.game_dto.CreateRoomRequestDto;
import com.seventeam.algoritmgameproject.web.dto.game_dto.EnterAndExitRoomRequestDto;
import com.seventeam.algoritmgameproject.domain.model.game.GameRoom;
import com.seventeam.algoritmgameproject.web.service.game_service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/game")
public class GameRoomController {

    private final GameService service;

    @GetMapping("/rooms")
    @Operation(summary = "방 목록 가져오기 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_OK, description = "방 목록 반환",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GameRoom.class)))}),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_FORBIDDEN, description = "로그인 필요", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_BADREQUEST, description = "요청 데이터 오류", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_SERVERERROR, description = "서버 오류", content = @Content)
    })
    @Parameters(value = {
            @Parameter(in = ParameterIn.PATH, name = "langIdx", description = "0:JAVA, 1:JS, 2:PYTHON3"),
            @Parameter(in = ParameterIn.PATH, name = "levelIdx", description = "0:EASY, 1:NORMAL, 2:HARD")
    })
    public ResponseEntity<?> getRooms(@RequestParam int langIdx, @RequestParam int levelIdx, @AuthenticationPrincipal UserDetailImpl userDetail) {
        return new ResponseEntity<>(service.findRooms(langIdx, levelIdx, userDetail.getUser().getUserId()), HttpStatus.OK);
    }


    @PostMapping("/room/create")
    @Operation(summary = "방 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_OK, description = "방 생성",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GameRoom.class))}),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_FORBIDDEN, description = "로그인 필요", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_BADREQUEST, description = "요청 데이터 오류", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_SERVERERROR, description = "서버 오류", content = @Content)
    })
    @Parameter(in = ParameterIn.PATH, name = "CreateRoomRequestDto",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateRoomRequestDto.class))})
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequestDto dto, @AuthenticationPrincipal UserDetailImpl userDetail) {
        log.info(dto.toString());
        return new ResponseEntity<>(service.createRoom(dto.getLangIdx(), dto.getLevelIdx(),userDetail.getUser()), HttpStatus.OK);
    }

    @PutMapping("/room/enter")
    @Operation(summary = "방 입장 처리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_OK, description = "입장 처리: gameroom 객체, 입장 불가: FASLE", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_FORBIDDEN, description = "로그인 필요", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_BADREQUEST, description = "요청 데이터 오류", content = @Content),
            @ApiResponse(responseCode = HttpCode.HTTPSTATUS_SERVERERROR, description = "서버 오류", content = @Content)
    })
    @Parameter(in = ParameterIn.PATH, name = "EnterRoomRequestDto",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = EnterAndExitRoomRequestDto.class))})
    public ResponseEntity<?> enterRoom(@RequestBody EnterAndExitRoomRequestDto dto, @AuthenticationPrincipal UserDetailImpl userDetail) {
        log.info("Enter:{}", dto.toString());
        if (service.isEnter(dto.getServer(), dto.getRoomId())) {
            return new ResponseEntity<>(service.enterRoom(dto,userDetail.getUser()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/room/exit")
    @ResponseBody
    @Operation(summary = "방 퇴장 처리 API")
    @Parameter(in = ParameterIn.PATH, name = "EnterAndExitRoomRequestDto",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = EnterAndExitRoomRequestDto.class))})
    public void exitRoom(@RequestBody EnterAndExitRoomRequestDto dto, @AuthenticationPrincipal UserDetailImpl userDetail) {
        log.info("Exit:{}", dto.toString());
        service.exitRoom(dto.getServer(),dto.getRoomId(),userDetail.getUser());
    }
}
