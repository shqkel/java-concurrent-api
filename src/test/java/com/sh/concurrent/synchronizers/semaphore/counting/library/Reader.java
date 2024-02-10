package com.sh.concurrent.synchronizers.semaphore.counting.library;

import lombok.Data;

@Data
public class Reader implements Runnable {
    private final Long id;
    private final Library library;
    public void read(Book book) {
        System.out.println("Reader-" + this.id + " 이/가 " + book + "을 읽는 중...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Book book = (Book) library.checkoutBook();
            read(book);
            library.returnBook(book);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
} 