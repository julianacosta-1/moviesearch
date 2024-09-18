package org.search.domain.event;

public interface SearchEventListener {
    void onSearchCompleted(SearchEvent event);
}