//package com.io.threegonew.controller;
//
//import com.io.threegonew.dto.BoardFileRequest;
//import com.io.threegonew.dto.BoardFileResponse;
//import com.io.threegonew.service.BoardFileService;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.*;
//import java.net.URLEncoder;
//
//@Controller
//@RequiredArgsConstructor
//public class BoardFileController {
//
//    private final BoardFileService boardFileService;
//
//    @GetMapping("/file/download")
//    public void downloadFile(@RequestParam() Integer id, HttpServletResponse response) throws Exception {
//        try {
//            // 파일 정보를 조회한다.
//            BoardFileResponse fileInfo = boardFileService.findById(id);
//            if(fileInfo == null) throw new FileNotFoundException("empty filedate.");
//
//            // 경로와 파일명으로 파일 객체를 생성한다.
//            File dFile = new File(fileInfo.getFilepath(), fileInfo.getSfile());
//
//            // 파일 길이를 가져온다.
//            int fSize = (int)dFile.length();
//
//            // 파일이 존재하면
//            if(fSize > 0) {
//                // 파일명을 URLEncoder 하여 attachment, Content-Disposition Header로 설정
//                String encodedFilename = "attachment; filename*=" + "UTF-8" + "''" + URLEncoder.encode(fileInfo.getOfile(), "UTF-8");
//
//                // ContentType 설정
//                response.setContentType("application/octet-stream; charset=utf-8");
//
//                // Header 설정
//                response.setHeader("Content-Disposition", encodedFilename);
//
//                // ContentLength 설정
//                response.setContentLengthLong(fSize);
//
//                BufferedInputStream in = null;
//                BufferedOutputStream out = null;
//
//                /* BufferedInputStream, BufferedOutputStream
//                *
//                * java.io의 가장 기본 파일 입출력 클래스
//                * 입력 스트림(통로)를 생성해줌
//                * 사용법은 간단하지만, 버퍼를 사용하지 않기 때문에 느림
//                * 속도 문제를 해결하기 위해 버퍼를 사용하는 다른 클래스와 같이 쓰는 경우가 많음
//                * */
//                in = new BufferedInputStream(new FileInputStream(dFile));
//                out = new BufferedOutputStream(response.getOutputStream());
//
//                try {
//                    byte[] buffer = new byte[4096];
//                    int bytesRead = 0;
//
//                    /*
//                    * 모두 현재 파일 포인터 위치를 기준으로 함(파일 포인터 앞의 내용은 없는 것 처럼 작동)
//                    * int read() : 1byte씩 내용을 읽어 정수로 반환
//                    * int read(byte[] b) : 파일 내용을 한번에 모두 읽어서 배열에 저장
//                    * int read(byte[] b, int off, int len) : 'len'길이만큼만 읽어서 배열의 'off'번째 위치부터 저장 */
//                    while((bytesRead = in.read(buffer)) != -1) {
//                        out.write(buffer, 0, bytesRead);
//                    }
//
//                    // 버퍼에 남은 내용이 있다면, 모두 파일에 출력
//                    out.flush();
//                } finally {
//                    /*
//                    * 현재 열려 in, out 스트림을 닫음
//                    * 메모리 누수를 방지하고 다른 곳에서 리소스 사용이 가능하게 만듬
//                    * */
//                    in.close();
//                    out.close();
//                }
//            } else {
//                throw new FileNotFoundException("empty filedata.");
//            }
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }
//
//    @PostMapping("/file/delete.ajax")
//    public String updateDeleteYn(Model model, BoardFileRequest boardFileRequest) throws Exception {
//        try {
//            model.addAttribute("result", boardFileService.updateDeleteYn(boardFileRequest.getIdArr()));
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//        return "jsonView";
//    }
//
//
//    // 포스트맨 테스트
////    private final String fileDir = System.getProperty("user.dir") // 현재 디렉토리 경로
////            + "/src/main/resources/static/files/"; // 파일이 저장될 폴더의 경로
////
////
////    @PostMapping("/upload")
////    public String saveFile(@RequestParam("files")MultipartFile file) throws IOException {
////        String fullPath = "";
////        if(!file.isEmpty()) {
////            fullPath = fileDir + file.getOriginalFilename();
////            file.transferTo(new File(fullPath));
////        }
////        return fullPath;
////    }
//}
