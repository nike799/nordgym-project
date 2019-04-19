package com.nordgym.service;

public interface UserEntryService {
    boolean checkInUser(Long userId);
    boolean removeLastEntry(Long entryId,Long userId);
}
