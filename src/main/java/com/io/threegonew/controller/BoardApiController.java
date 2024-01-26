package com.io.threegonew.controller;

import com.io.threegonew.domain.Board;
import com.io.threegonew.dto.AddBoardRequest;
import com.io.threegonew.dto.BoardResponse;
import com.io.threegonew.dto.UpdateBoardRequest;
import com.io.threegonew.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardApiController {
    private final BoardService boardService;



    @GetMapping("/api/board")
    public ResponseEntity<List<BoardResponse>> findAllPost() {
        List<BoardResponse> board = boardService.findAll()
                .stream()
                .map(BoardResponse::new)
                .toList();

        return ResponseEntity.ok().body(board);
    }

    @PostMapping("/api/board")
    public ResponseEntity<Board> addPost(@RequestBody AddBoardRequest request) {
        Board savedBoard = boardService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }

    @GetMapping("/api/board/{bid}")
    public ResponseEntity<BoardResponse> findPost(@PathVariable Integer bid) {
        Board board  = boardService.findById(bid);

        return ResponseEntity.ok().body(new BoardResponse(board));
    }

    @DeleteMapping("/api/board/{bid}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer bid) {
        boardService.delete(bid);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/board/{bid}")
    public ResponseEntity<Board> updateBoard(@PathVariable Integer bid, @RequestBody UpdateBoardRequest request) {
        Board updateBoard = boardService.update(bid, request);

        return ResponseEntity.ok().body(updateBoard);
    }
}