package sample.cafekiosk.spring.domain.history.mail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendRepository extends JpaRepository<MailSendHistory, Long> {


}
