package org.search.domain;

public class Query {
    private final String value;

    public Query(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidQueryException("Query cannot be null or empty.");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return value.equals(query.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}

