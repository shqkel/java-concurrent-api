package com.sh.concurrent.synchronizers.semaphore.counting.library;

import lombok.Data;

@Data
public class Book {
    private final Long id;
    private final BookInfo bookInfo;
    private boolean available = true;
}