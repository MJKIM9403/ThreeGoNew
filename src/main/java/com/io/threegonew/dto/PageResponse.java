package com.io.threegonew.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/*페이징 처리를 위한 page 응답 DTO*/
/*페이징 처리할 DTO리스트를 담아 반환 후 view단에서 spring EL로 필요한 값을 받아 사용해주세요.*/
/*주의: page index가 0부터 시작합니다.*/
@Getter
@ToString
public class PageResponse<E> {
    private int page;
    private int size;
    private int last;
    private long total;

    /* 시작, 끝 페이지 */
    private int start;
    private int end;

    /* 이전, 다음 페이지 여부 */
    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    //builder Builder() 를 다른 이름으로 네이밍 할 때 사용함.
    @Builder(builderMethodName = "withAll")
    public PageResponse(List<E> dtoList, int page, int size, int totalPages, long total){
        this.page = page;
        this.size = size;

        this.total = total;
        this.dtoList = dtoList;

        /* 마지막 페이지, 시작 페이지 번호 계산*/
        this.end = (int)(Math.ceil((this.page + 1) / 5.0))*5 -1;
        this.start = this.end - 4;

        this.last = totalPages == 0 ? 0 : totalPages - 1;

        this.end = Math.min(end, this.last);
        this.prev = this.start > 1;
        this.next = this.end < this.last;

        System.out.println("totalPage: " + totalPages);
        System.out.println("last: " + last);
        System.out.println("end: " + end);
    }

}
