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
        List<BoardViewResponse> board = boardService.findAll().stream()
                .map(BoardViewResponse::new)
                .toList();
        model.addAttribute("board", board);

        return "board/bbs";
    }

    @GetMapping("/board/{bid}")
    public String getBoard(@PathVariable Long bid, Model model){
        Board board = boardService.findById(bid);
        boardService.updateVisit(board.getBid());

        model.addAttribute("board", new BoardViewResponse(board));

        return "board/view";
    }

    @GetMapping("/write")
    public String newBoard(@RequestParam(required = false) Long bid, Model model) {
        if(bid == null) {
            model.addAttribute("board",new BoardViewResponse());
        } else {
            Board board = boardService.findById(bid);
            model.addAttribute("board",new BoardViewResponse(board));
        }
        return "board/write";
    }
}
