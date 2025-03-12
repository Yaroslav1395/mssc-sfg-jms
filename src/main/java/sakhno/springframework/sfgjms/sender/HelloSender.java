package sakhno.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sakhno.springframework.sfgjms.config.JmsConfig;
import sakhno.springframework.sfgjms.model.HelloWorldMessage;

import java.util.UUID;

@Component
public class HelloSender {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HelloSender(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Данный метод отправляет в очередь MY_QUEUE сообщение
     */
    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        System.out.println("Я послал сообщение");
        HelloWorldMessage helloWorldMessage = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello World!")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, helloWorldMessage);
        System.out.println("Сообщение отправлено");
    }

    /**
     * Данный метод отправляет сообщение в очередь MY_SEND_RCV_QUEUE и ожидает ответа. Так 
     * как при использовании метода sendAndReceive Spring не может автоматически обработать объект через
     * MessageConverter необходимо создавать сообщение вручную
     * @throws JMSException - в случае если не удалось преобразовать объект в JSON строку
     */
    @Scheduled(fixedRate = 2000)
    public void sendReceiveMessage() throws JMSException {
        HelloWorldMessage helloWorldMessage = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message recivedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            Message helloMessage;
            try {
                helloMessage = session.createTextMessage(objectMapper.writeValueAsString(helloWorldMessage));
                helloMessage.setStringProperty("_type", "sakhno.springframework.sfgjms.model.HelloWorldMessage");
                System.out.println("Отправка привет");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
        });
        System.out.println(recivedMessage.getBody(String.class));
    }
}
