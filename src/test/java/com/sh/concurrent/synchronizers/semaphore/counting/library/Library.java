
package com.sh.concurrent.synchronizers.semaphore.counting.library;

import java.util.concurrent.Semaphore;

public class Library {
    private static final int MAX_PERMIT = 3;
    private final Semaphore semaphore = new Semaphore(MAX_PERMIT, true);
    private Book[] books = {new Book(1L, BookInfo.대머리여가수), new Book(2L, BookInfo.대머리여가수), new Book(3L, BookInfo.대머리여가수)};

    // Book is being issued to reader
    public Book checkoutBook() throws InterruptedException {
        semaphore.acquire();
        return getNextAvailableBook();
    }

    private  Book getNextAvailableBook() {
        Book book = null;
        for (int i = 0; i < MAX_PERMIT; ++i) {
            book = books[i];
            if (book.isAvailable()) {
                book.setAvailable(false);
                System.out.println(book + "을 대여했습니다...");
                break;
            }
        }
        return book;
    }

    //Book is being returned to library
    public void returnBook(Book book) {
        if (markAsAvailableBook(book))
            semaphore.release();
    }

    private  boolean markAsAvailableBook(Book book) {
        boolean flag = false;
        for (int i = 0; i < MAX_PERMIT; ++i) {
            if (book == books[i]) {
                if (!book.isAvailable()) {
                    book.setAvailable(true);
                    flag = true;
                    System.out.println(book + "을 반환했습니다...");
                }
                break;
            }
        }
        return flag;
    }
} 