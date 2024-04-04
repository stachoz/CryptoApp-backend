package com.example.cryptoapp.post.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResponse<T> {
    private final List<T> content;
    private final int totalPages;
    private final long totalElements;
    private final int pageNumber;
    private final int pageSize;

    public PagedResponse(int totalPages, long totalElements, int page, int size,List<T> content){
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageNumber = page;
        this.pageSize = size;
    }
}
