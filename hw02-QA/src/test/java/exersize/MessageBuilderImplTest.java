package exersize;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Класс MessageBuilderImpl")
@ExtendWith(MockitoExtension.class)
class MessageBuilderImplTest {

    @Mock
    private MessageTemplateProvider provider;
    private MessageBuilder messageBuilder;

    private static final String TEMPLATE = "template";
    private static final String MESSAGE_TEXT = "message text";
    private static final String SIGNATURE = "signature";
    private static final String TEMPLATE_NAME = "template name";

    @BeforeEach
    void setUp() {
        messageBuilder = new MessageBuilderImpl(provider);
    }

    @Test
    @DisplayName("возвращает нужное")
    void buildMessageTest1() {
        // вызови метод у заглушки с любым аргументом и пусть он вернет заданное
        Mockito.when(provider.getMessageTemplate(Mockito.any())).thenReturn(TEMPLATE);

        String expectedMessage = String.format(TEMPLATE, MESSAGE_TEXT, SIGNATURE);
        String actualMessage = messageBuilder.buildMessage(null, MESSAGE_TEXT, SIGNATURE);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("вызывается нужное количество раз")
    void buildMessageTest2() {
        // вызови метод у заглушки с аргументом и пусть он вернет заданное
        Mockito.when(provider.getMessageTemplate(TEMPLATE_NAME)).thenReturn(" ");

        messageBuilder.buildMessage(TEMPLATE_NAME, null, null);

        // проверь, что у провайдера был вызван 1 раз метод с определенным аргументом
        Mockito.verify(provider, Mockito.times(1)).getMessageTemplate(TEMPLATE_NAME);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("выкидывает исключение")
    void buildMessageTest3(String templateName) {
        Assertions.assertThrows(TemplateNotFoundException.class,
                () -> messageBuilder.buildMessage(templateName, "", ""));
    }
}