package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    // @Spy
    @Mock
    MailSendClient mailSendClient;
    @Mock
    MailSendRepository mailSendRepository;
    @InjectMocks
    MailService mailService;

    @Test
    @DisplayName("메일 전송 테스트")
    void sendMail() {
        // given
        // MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
        // MailSendRepository mailSendRepository = Mockito.mock(MailSendRepository.class);

        // MailService mailService = new MailService(mailSendClient, mailSendRepository);

        Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        Mockito.verify(mailSendRepository, times(1)).save(any(MailSendHistory.class));
    }

    @Test
    @DisplayName("메일전송 테스트 @Spy")
    void sendMailSpy() {
        // given
        // @Spy는 실제 객체와 일부만 stubbing하여 사용할 수 있다.
        // mailSendClient의 sendEmail()만 stubbing 하고
        // 나머지에 대해서는 실제 객체를 사용
        doReturn(true)
                .when(mailSendClient)
                .sendEmail(anyString(), anyString(), anyString(), anyString());

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        Mockito.verify(mailSendRepository, times(1)).save(any(MailSendHistory.class));
    }

    @Test
    @DisplayName("메일전송 테스트 @Spy")
    void sendMailBDDMockito() {
        // given
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        Mockito.verify(mailSendRepository, times(1)).save(any(MailSendHistory.class));
    }
}