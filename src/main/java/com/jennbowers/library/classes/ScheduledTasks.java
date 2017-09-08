package com.jennbowers.library.classes;

import com.jennbowers.library.interfaces.BookRequestRepository;
import com.jennbowers.library.models.BookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {
    @Scheduled(cron = "0 1 0 * * ?")
//    This should run every day at one minute after midnight
//    @Scheduled(cron = "* * * * * ?")
    public void checkOverdue(){
//        Create a date variable with the new current date
        log.info("Working!");

        Date todaysDate = new Date();
        java.sql.Date sqlTodaysDate = new java.sql.Date(todaysDate.getTime());
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(todaysDate);
        int todayYear = todayCal.get(Calendar.YEAR);
        int todayMonth = todayCal.get(Calendar.MONTH);
        int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
//              Method here to go into database and find all books requests that are active                 and check their due date to see if it is equal to the new current date
        List<BookRequest> dueBooks = bookRequestRepo.findAllByActive(true);
//              if request is overdue then the request is set to inactive
        for(BookRequest request : dueBooks) {
            Date dueDate = request.getDue();
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(todaysDate);
            int dueYear = dueCal.get(Calendar.YEAR);
            int dueMonth = dueCal.get(Calendar.MONTH);
            int dueDay = dueCal.get(Calendar.DAY_OF_MONTH);
            if(dueYear == todayYear && dueMonth == todayMonth && dueDay == todayDay) {
                log.info("request" + request);
                request.setActive(false);
                log.info("set to false complete");
                bookRequestRepo.save(request);
            }
        }
        System.out.println("done!");
//              CHECK!
        List<BookRequest> checkBooks = bookRequestRepo.findAllByActive(true);
        for(BookRequest request : checkBooks) {
            Date dueDate = request.getDue();
            log.info("Today's date is: " + sqlTodaysDate + " and due dates left after check are :" + dueDate);
        }

    }

    @Autowired
    BookRequestRepository bookRequestRepo;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
//        STRETCH GOAL: figure out way to send email to email address on file to say, your book was overdue, please return the book to the person you borrowed it from!
}
