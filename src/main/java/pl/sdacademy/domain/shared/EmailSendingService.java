package pl.sdacademy.domain.shared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendingService {

    private final JavaMailSender emailSender;
    private final EmailSubjectMessageResolver emailSubjectMessageResolver;

    public void sendSimpleMessage(String to, EmailSubject subject, String content) {
        String resolvedSubject = emailSubjectMessageResolver.resolveMessageFor(subject)
                                                            .toBlocking()
                                                            .single();
        sendSimpleMessage(to, resolvedSubject, content);
    }

    public void sendSimpleMessage(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
    }

}
