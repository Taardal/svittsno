package no.svitts.core.search;

public class SearchCriteria {

    private SearchKey searchKey;
    private String value;
    private int limit;

    public SearchCriteria(SearchKey searchKey, String value, int limit) {
        this.searchKey = searchKey;
        this.value = value;
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "searchKey=" + searchKey +
                ", value='" + value + '\'' +
                ", limit=" + limit +
                '}';
    }

    public SearchKey getKey() {
        return searchKey;
    }

    public String getValue() {
        return value;
    }

    public int getLimit() {
        return limit;
    }
}
