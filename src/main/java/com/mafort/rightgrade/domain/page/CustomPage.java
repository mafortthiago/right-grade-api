package com.mafort.rightgrade.domain.page;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
public class CustomPage<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public CustomPage(Page<T> page){
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
    public <R> CustomPage<R> map(Function<? super T, ? extends R> converter) {
        List<R> convertedContent = (List<R>) this.content.stream().map(converter).toList();
        return new CustomPage<>(convertedContent, this.pageNumber, this.pageSize, this.totalElements);
    }

    public CustomPage(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }
}
