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

    @GetMapping("/api/board/{bId}")
    public ResponseEntity<BoardResponse> findPost(@PathVariable Long bId) {
        Board board  = boardService.findById(bId);

        return ResponseEntity.ok().body(new BoardResponse(board));
    }

    @DeleteMapping("/api/board/{bId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long bId) {
        boardService.delete(bId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/board/{bId}")
    public ResponseEntity<Board> updatePost(@PathVariable Long bId, @RequestBody UpdateBoardRequest request) {
        Board updateBoard = boardService.update(bId, request);

        return ResponseEntity.ok().body(updateBoard);
    }
}
