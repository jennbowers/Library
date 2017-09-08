package com.jennbowers.library.classes;

import com.jennbowers.library.interfaces.BookRequestRepository;
import com.jennbowers.library.models.BookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    BookRequestRepository bookRequestRepo;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    //    This should run every day at one minute after midnight
//    @Scheduled(cron = "0 1 0 * * ?")
    @Scheduled(cron = "* * * * *")
    public void checkOverdue(){
//        Create a date variable with the new current date
        log.info("The time is now SHUT UP O CLOCK");

        Date todaysDate = new Date();
        java.sql.Date sqlTodaysDate = new java.sql.Date(todaysDate.getTime());
//        Method here to go into database
//        find all books requests that are active
//        check their due date to see if it is equal to the new current date
        List<BookRequest> dueBooks = bookRequestRepo.findAllByActiveEquals(sqlTodaysDate);
//        if request is overdue then the request is set to inactive
        for(BookRequest request : dueBooks) {
            request.setActive(false);
            bookRequestRepo.save(request);
        }
        System.out.println("done!");
//        STRETCH GOAL: figure out way to send email to email address on file to say, your book was overdue, please return the book to the person you borrowed it from!
    }
}
