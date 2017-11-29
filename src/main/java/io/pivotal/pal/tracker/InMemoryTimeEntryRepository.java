package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long,TimeEntry> inMemoryTimeCacheMap = new HashMap<Long, TimeEntry>();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        Long maxEntries = new Long(inMemoryTimeCacheMap.size());
        timeEntry.setId(maxEntries+1);
        inMemoryTimeCacheMap.put(timeEntry.getId(),timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return inMemoryTimeCacheMap.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(inMemoryTimeCacheMap.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        inMemoryTimeCacheMap.replace(id,timeEntry);
        timeEntry.setId(id);
        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        inMemoryTimeCacheMap.remove(id);
    }


}
