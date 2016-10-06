package no.svitts.core.search;

public class Search {

    private String query;
    private SearchKey searchKey;
    private int limit;
    private int offset;

    public Search(String query, SearchKey searchKey) {
        this.query = query;
        this.searchKey = searchKey;
        limit = 10;
        offset = 0;
    }

    public Search(String query, SearchKey searchKey, int limit, int offset) {
        this.query = query;
        this.searchKey = searchKey;
        this.limit = limit;
        this.offset = offset;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SearchKey getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(SearchKey searchKey) {
        this.searchKey = searchKey;
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
