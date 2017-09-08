package com.jennbowers.library.classes;

import com.jennbowers.library.interfaces.BookRequestRepository;
import com.jennbowers.library.models.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;


public class ScheduledTasks {
    @Autowired
    BookRequestRepository bookRequestRepo;

//    This should run every day at one minute after midnight
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkOverdue(){
//        Create a date variable with the new current date
        Date todaysDate = new Date();
//        Method here to go into database
//        find all books requests that are active
//        check their due date to see if it is equal to the new current date
        List<BookRequest> dueBooks = bookRequestRepo.findAllByActiveEquals(todaysDate);
//        if request is overdue then the request is set to inactive
        for(BookRequest request : dueBooks) {
            request.setActive(false);
            bookRequestRepo.save(request);
        }
//        STRETCH GOAL: figure out way to send email to email address on file to say, your book was overdue, please return the book to the person you borrowed it from!
    }
}
