package sakhno.springframework.sfgjms.listener;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sakhno.springframework.sfgjms.config.JmsConfig;
import sakhno.springframework.sfgjms.model.HelloWorldMessage;

import java.util.UUID;

@Component
public class HelloMessageListener {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public HelloMessageListener(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    /**
     * Данный метод прослушивает очередь MY_QUEUE
     * @param helloWorldMessage - объект, который приходит из очереди
     * @param headers - все заголовки JMS-сообщения (_type, JMSCorrelationID)
     * @param message - исходное сообщение JMS (низкоуровневые данные)
     */
    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message) {
        System.out.println("Я получил сообщение" + helloWorldMessage + ">");
    }

    /**
     * Данный метод прослушивает очередь MY_SEND_RCV_QUEUE и отправляет сообщение в ответ
     * @param helloWorldMessage - - объект, который приходит из очереди
     * @param headers - все заголовки JMS-сообщения (_type, JMSCorrelationID)
     * @param message - исходное сообщение JMS (низкоуровневые данные)
     * @throws JMSException - в случае если не удалось привести к JSON строке объект для ответа
     */
    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage payload = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("World")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payload);
        System.out.println("Я получил сообщение" + helloWorldMessage + ">");
    }
}
