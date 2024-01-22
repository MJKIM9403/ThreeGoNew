package com.io.threegonew.controller;

import com.io.threegonew.domain.Board;
import com.io.threegonew.dto.BoardViewResponse;
import com.io.threegonew.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardViewController {
    private final BoardService boardService;

    @GetMapping("/board")
    public String getBoards(Model model) {
        List<BoardViewResponse> boards = boardService.findAll().stream()
                .map(BoardViewResponse::new)
                .toList();
        model.addAttribute("boards", boards);

        return "board/bbs";
    }

    @GetMapping("/board/b_id={bId}")
    public String getBoard(@PathVariable Integer bId, Model model){
        Board board = boardService.findById(bId);
        model.addAttribute("board", new BoardViewResponse(board));

        return "board/view";
    }

    @GetMapping("/write")
    public String newBoard(@RequestParam(required = false) Integer bId, Model model) {
        if(bId == null) {
            model.addAttribute("board", new BoardViewResponse());
        } else {
            Board board = boardService.findById(bId);
            model.addAttribute("board",new BoardViewResponse(board));
        }
        return "board/write";
    }
}
