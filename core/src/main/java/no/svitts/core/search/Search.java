package no.svitts.core.search;

public abstract class Search {

    private int limit;
    private int offset;

    public Search() {
        limit = 10;
        offset = 0;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
