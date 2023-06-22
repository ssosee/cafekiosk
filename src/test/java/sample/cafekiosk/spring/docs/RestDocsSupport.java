package sample.cafekiosk.spring.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * REST Docs 셋팅
 */
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
// @SpringBootTest
public abstract class RestDocsSupport {
    protected MockMvc mockMvc;
    protected ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        // @SpringBootTest를 하지 않는 이유
        // 문서를 작성할 때 Spring을 동작시킬 필요는 없다.
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
                .apply(documentationConfiguration(provider))
                .build();
    }

    protected abstract Object initController();
}
