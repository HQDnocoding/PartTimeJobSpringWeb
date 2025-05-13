/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.event;

import com.myweb.pojo.Follow;
import com.myweb.pojo.Job;
import com.myweb.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Component
public class JobEventListener {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FollowService followService;

    @Async
    @EventListener
    public void handleJobCreatedEvent(JobCreatedEvent event) throws MessagingException {
        Job job = event.getJob();
        List<Follow> followers = followService.getFollowers(job.getCompanyId().getId());

        for (Follow follow : followers) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(follow.getCandidateId().getEmail());
            helper.setSubject("Tin tuyển dụng mới từ " + job.getCompanyId().getName());
            helper.setText(
                    "<h3>Tin tuyển dụng mới!</h3>" +
                    "<p>Công ty <strong>" + job.getCompanyId().getName() + "</strong> vừa đăng tuyển vị trí: <strong>" + job.getJobName() + "</strong>.</p>" +
                    "<p><a href='http://your-domain.com/job/" + job.getId() + "'>Xem chi tiết</a></p>",
                    true
            );
            mailSender.send(message);
        }
    }
}