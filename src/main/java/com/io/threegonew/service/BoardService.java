package com.io.threegonew.service;

import com.io.threegonew.domain.Board;
import com.io.threegonew.dto.AddBoardRequest;
import com.io.threegonew.dto.UpdateBoardRequest;
import com.io.threegonew.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시판 글 추가
    public Board save(AddBoardRequest request) {
        return boardRepository.save(request.toEntity());
    }

    // 게시판 글 전체 조회
    public List<Board> findAll() {
        return boardRepository.findAll(Sort.by("bPostdate").descending());
    }

    public Board findById(Integer bId) {
        return boardRepository.findById(bId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + bId));
    }

    // 게시판 글 삭제
    public void delete(int bId) {
        boardRepository.deleteById(bId);
    }

    @Transactional
    public Board update(int bId, UpdateBoardRequest request) {
        Board board = boardRepository.findById(bId).orElseThrow(() ->
                new IllegalArgumentException("not found : " + bId));

        board.update(request.getBTitle(), request.getBContent());

        return board;
    }
}
