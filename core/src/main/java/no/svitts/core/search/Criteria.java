package no.svitts.core.search;

import java.util.HashMap;
import java.util.Map;

public class Criteria {

    private Map<CriteriaKey, String> criteria;
    private int limit;
    private int offset;

    public Criteria() {
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

    public void addCriteria(CriteriaKey criteriaKey, String criteria) {
        this.criteria.put(criteriaKey, criteria);
    }

    public String getCriteria(CriteriaKey criteriaKey) {
        return criteria.get(criteriaKey);
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
