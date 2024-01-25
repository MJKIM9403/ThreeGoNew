//package com.io.threegonew.domain;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.time.LocalDateTime;
//
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class BoardFile {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    private Integer bid;
//    private String ofile;
//    private String sfile;
//    private String filepath;
//    private String deleteyn;
//
//    @Builder
//    public BoardFile(Integer id, Integer bid, String ofile, String sfile, String filepath, String deleteyn) {
//        this.id = id;
//        this.bid = bid;
//        this.ofile = ofile;
//        this.sfile = sfile;
//        this.filepath = filepath;
//        this.deleteyn = deleteyn;
//    }
//
//    public void setBoard(Board board) {
//        this.board = board;
//    }
//}
