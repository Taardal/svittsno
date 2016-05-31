package no.svitts.core.search;

import java.util.HashMap;
import java.util.Map;

public class Criteria {

    private Map<SearchKey, String> criteria;
    private int limit;
    private int offset;

    public Criteria(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
        criteria = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "criteria=" + criteria +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }

    public void addCriteria(SearchKey searchKey, String criteria) {
        this.criteria.put(searchKey, criteria);
    }

    public String getCriteria(SearchKey searchKey) {
        return criteria.get(searchKey);
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

}
