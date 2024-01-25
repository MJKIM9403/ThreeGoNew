//package com.io.threegonew.repository;
//
//import com.io.threegonew.domain.BoardFile;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//public interface BoardFileRepository extends JpaRepository<BoardFile, Integer> {
//
//    static final String SELECT_FILE_ID = "SELECT ID FROM board_file" + "WHERE B_ID = : b_id AND DELETE_YN != 'Y'";
//
//    static final String UPDATE_DELETE_YN = "UPDATE board_file" + "SET DELETE_YN = 'Y'" + "WHERE ID IN (:deleteIdList)";
//
//    static final String DELETE_BOARD_FILE_YN = "UPDATE board_file" + "SET DELETE_YN = 'Y'" + "WHERE B_ID IN (:boardIdList)";
//
//    @Query(value = SELECT_FILE_ID, nativeQuery = true)
//    public List<Integer> findByBid(@Param("b_id")Integer bid);
//
//    @Transactional
//    @Modifying
//    @Query(value = UPDATE_DELETE_YN, nativeQuery = true)
//    public int updateDeleteYn(@Param("deleteIdList") Integer[] deleteIdList);
//
//    @Transactional
//    @Modifying
//    @Query(value = DELETE_BOARD_FILE_YN, nativeQuery = true)
//    public int deleteBoardFileYn(@Param("boardIdList") Integer[] boardIdList);
//
//}
