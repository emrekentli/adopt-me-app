package com.emrekentli.adoptme.model.response;

public class Pageable {
    private int pageNumber;
    private int pageSize;
    private int offset;
    private boolean paged;
    private boolean unpaged;

    public Pageable(int pageNumber, int pageSize, int offset, boolean paged, boolean unpaged) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.offset = offset;
        this.paged = paged;
        this.unpaged = unpaged;
    }

    public Pageable() {
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isPaged() {
        return paged;
    }

    public boolean isUnpaged() {
        return unpaged;
    }
}
