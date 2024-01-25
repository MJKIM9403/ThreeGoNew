//package com.io.threegonew.service;
//
//import java.io.File;
//import java.security.SecureRandom;
//import java.util.*;
//import java.util.Map.Entry;
//
//import com.io.threegonew.domain.BoardFile;
//import com.io.threegonew.dto.BoardFileResponse;
//import com.io.threegonew.repository.BoardFileRepository;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//
//
//@RequiredArgsConstructor
//@Service
//public class BoardFileService {
//    private final BoardFileRepository boardFileRepository;
//
//    public BoardFileResponse findById(Integer id) throws Exception {
//        return new BoardFileResponse(boardFileRepository.findById(id).get());
//    }
//
//    public List<Integer> findByBid(Integer bid) throws Exception {
//        return boardFileRepository.findByBid(bid);
//    }
//
//    public boolean uploadFile(MultipartHttpServletRequest multiRequest, Integer bid) throws Exception {
//        if(bid == null) throw new NullPointerException("empty bid");
//
//        // 파라미터 이름을 키로 파라미터에 해당하는 파일 정보를 값으로 하는 Map을 가져온다.
//        Map<String, MultipartFile> files = multiRequest.getFileMap();
//
//        // files.entrySet()의 요소를 읽어온다.
//        Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
//
//        MultipartFile mFile;
//
//        String saveFilePath = "", randomFileName = "";
//
//        Calendar cal = Calendar.getInstance();
//
//        List<Integer> resultList = new ArrayList<Integer>();
//
//        while(itr.hasNext()) {
//            Entry<String, MultipartFile> entry = itr.next();
//            mFile = entry.getValue();
//            int fileSize = (int)mFile.getSize();
//            if(fileSize > 0) {
//                String filePath = System.getProperty("user.dir") // 현재 디렉토리 경로
//            + "/src/main/resources/static/files/";
//
//                // 파일 업로드 경로 + 현재 년월(월별 관리)
//                filePath = filePath + File.separator + String.valueOf(cal.get(Calendar.YEAR)) + File.separator + String.valueOf(cal.get(Calendar.MONTH) + 1);
//                randomFileName = "FILE_" + RandomStringUtils.random(8,0,0,false,true,null,new SecureRandom());
//
//                String realFileName = mFile.getOriginalFilename();
//                String fileExt = realFileName.substring(realFileName.lastIndexOf(".") + 1);
//                String saveFileName = randomFileName + "." + fileExt;
//
//                BoardFile boardFile = BoardFile.builder()
//                        .bid(bid)
//                        .ofile(realFileName)
//                        .sfile(saveFileName)
//                        .filepath(filePath)
//                        .deleteyn("N")
//                        .build();
//
//                resultList.add(boardFileRepository.save(boardFile).getId());
//            }
//        }
//        return (files.size() == resultList.size()) ? true : false;
//    }
//
//    public int updateDeleteYn(Integer[] deleteIdList) throws Exception {
//        return boardFileRepository.updateDeleteYn(deleteIdList);
//    }
//
//    public int deleteBoardFileYn(Integer[] boardIdList) throws Exception {
//        return boardFileRepository.deleteBoardFileYn(boardIdList);
//    }
//}
