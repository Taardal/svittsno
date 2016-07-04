package no.svitts.core.criteria;

import java.util.HashMap;
import java.util.Map;

public class Criteria {

    private Map<CriteriaKey, String> criteria;
    private int limit;
    private int offset;

    public Criteria() {
        criteria = new HashMap<>();
        limit = 10;
        offset = 0;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "criteria=" + criteria +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }

    public String get(CriteriaKey criteriaKey) {
        return criteria.get(criteriaKey);
    }

    public void add(CriteriaKey criteriaKey, String value) {
        criteria.put(criteriaKey, value);
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
