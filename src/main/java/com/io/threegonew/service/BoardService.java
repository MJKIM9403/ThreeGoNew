package com.io.threegonew.service;

import com.io.threegonew.domain.Board;
import com.io.threegonew.dto.AddBoardRequest;
import com.io.threegonew.dto.UpdateBoardRequest;
import com.io.threegonew.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileService boardFileService;

    // 게시판 글 추가
//    public Board save(AddBoardRequest request) {
//
//        return boardRepository.save(request.toEntity());
//    }
    // 파일 첨부 시 경로 받아오기

    public Board save(AddBoardRequest request, MultipartHttpServletRequest multiRequest) throws Exception {

//        // 파일 업로드 처리 시작
//        String projectPath = System.getProperty("user.dir") // 현재 디렉토리 경로
//                + "/src/main/resources/static/files"; // 파일이 저장될 폴더의 경로
//
//        UUID uuid = UUID.randomUUID(); // 랜덤으로 식별자를 생성
//        String fileName = uuid + "_" + file.getOriginalFilename(); // UUID와 파일이름을 포함된 파일 이름으로 저장
//        File saveFile = new File(projectPath, fileName); // projectPath는 위에서 작성한 경로, name은 전달받을 이름
//
//        file.transferTo(saveFile);
//        request.toEntity().updateBofile(String.valueOf(saveFile)); // DB에 파일 이름 저장
//        request.toEntity().updateBsfile("/files/"+fileName); // DB에 파일 경로 저장

        Board result = boardRepository.save(request.toEntity());
        boolean resultFlag = false;

        if(result != null) {
            boardFileService.uploadFile(multiRequest, result.getBid());
            resultFlag = true;
        }

        System.out.println(resultFlag);
        return result;


    }



    // 게시판 글 전체 조회(최신순 조회)
    public List<Board> findAll() {
        return boardRepository.findAll(Sort.by("bpostdate").descending());
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
    public Board findById(Integer bid) {
        return boardRepository.findById(bid)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + bid));
    }

    // 게시판 글 삭제
    public void delete(Integer bid) {
        boardRepository.deleteById(bid);
    }

    // 조회수 증가시키기
    @Transactional
    public void updateVisit(Integer bid) {
        Board board = boardRepository.findById(bid).orElseThrow(() ->
                new IllegalArgumentException("not found : " + bid));
        board.updateVisitCount(board.getBvisitcount() + 1);
    }


    // 게시글 업데이트
    @Transactional
    public Board update(Integer bid, UpdateBoardRequest request) {
        Board board = boardRepository.findById(bid).orElseThrow(() ->
                new IllegalArgumentException("not found : " + bid));


        board.update(request.getBtitle(), request.getBcontent());

        return board;
    }

}
