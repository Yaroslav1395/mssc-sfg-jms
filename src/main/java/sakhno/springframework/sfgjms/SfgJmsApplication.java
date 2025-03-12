package sakhno.springframework.sfgjms;

import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SfgJmsApplication {

    /**
     * Создает и запускает встроенный ActiveMQ сервер.
     * Использует ActiveMQServer из Apache ActiveMQ Artemis.
     * Работает в памяти (in-vm) — без необходимости внешнего брокера сообщений.
     * Отключает персистентность сообщений (setPersistenceEnabled(false)) — они теряются после перезапуска.
     * Отключает безопасность (setSecurityEnabled(false)) — сообщения передаются без аутентификации.
     * Устанавливает журнал сообщений в target/data/journal.
     * @param args - аргументы
     * @throws Exception - ошибка
     */
    public static void main(String[] args) throws Exception {
        ActiveMQServer server = ActiveMQServers.newActiveMQServer(new ConfigurationImpl()
                .setPersistenceEnabled(false)
                .setJournalDirectory("target/data/journal")
                .setSecurityEnabled(false)
                .addAcceptorConfiguration("invm", "vm://0"));

        server.start();
        SpringApplication.run(SfgJmsApplication.class, args);
    }

}
