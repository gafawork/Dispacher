package com.example.visitor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class OperacaoVisitorProcessorTest {

    @Test
    void shouldExecuteOperacaoXUsingVisitor() {
        var dispatcher = new OperacaoVisitorProcessor();

        var logs = captureLogs(() -> dispatcher.executar(new OperacaoX()));

        assertThat(logs)
                .anySatisfy(message -> assertThat(message).contains("Executando operação X"));
    }

    @Test
    void shouldLogWhenOperationDoesNotSupportTheRequestedCapability() {
        var dispatcher = new OperacaoVisitorProcessor();

        var logs = captureLogs(() -> dispatcher.executar(new OperacaoY()));

        assertThat(logs)
                .anySatisfy(message -> assertThat(message).contains("não possui a funcionalidade 'executar'"));
    }

    @Test
    void shouldConfirmOperacaoYUsingVisitor() {
        var dispatcher = new OperacaoVisitorProcessor();

        var logs = captureLogs(() -> dispatcher.confirmar(new OperacaoY()));

        assertThat(logs)
                .anySatisfy(message -> assertThat(message).contains("Confirmando operação Y"));
    }

    private static List<String> captureLogs(Runnable action) {
        Logger logger = Logger.getLogger("com.example.visitor");
        CapturingHandler handler = new CapturingHandler();
        Level previousLevel = logger.getLevel();
        boolean previousUseParentHandlers = logger.getUseParentHandlers();

        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        try {
            action.run();
            return handler.messages();
        } finally {
            logger.removeHandler(handler);
            logger.setLevel(previousLevel);
            logger.setUseParentHandlers(previousUseParentHandlers);
        }
    }

    private static final class CapturingHandler extends Handler {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            messages.add(record.getLevel() + ": " + record.getMessage());
        }

        @Override
        public void flush() {
            // nothing to flush
        }

        @Override
        public void close() {
            // nothing to close
        }

        List<String> messages() {
            return messages;
        }
    }
}
