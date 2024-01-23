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

    // 게시판 글 전체 조회(최신순 조회)
    public List<Board> findAll() {
        return boardRepository.findAll(Sort.by("bPostdate").descending());
    }

//    // 게시판 글 추천순 조회
//    public List<Board> findAllLikes() {
//        return boardRepository.findAll(Sort.by("likes").descending());
//    }
//
//    // 게시판 글 조회순 조회
//    public List<Board> findAllViews() {
//        return boardRepository.findAll(Sort.by("visitCount").descending());
//    }

    // 글 아이디로 조회하기
    public Board findById(Long bId) {
        return boardRepository.findById(bId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + bId));
    }

    // 게시판 글 삭제
    public void delete(Long bId) {
        boardRepository.deleteById(bId);
    }

    @Transactional
    public Board update(Long bId, UpdateBoardRequest request) {
        Board board = boardRepository.findById(bId).orElseThrow(() ->
                new IllegalArgumentException("not found : " + bId));

        board.update(request.getBTitle(), request.getBContent());

        return board;
    }
}
