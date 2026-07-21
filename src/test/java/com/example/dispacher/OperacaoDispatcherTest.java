package com.example.dispacher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperacaoDispatcherTest {

    @Mock
    private OperacaoXService operacaoXService;

    @Mock
    private OperacaoYService operacaoYService;

    private Logger logger;
    private Level previousLevel;
    private boolean previousUseParentHandlers;
    private CapturingHandler handler;

    @BeforeEach
    void setUpLogger() {
        logger = Logger.getLogger(OperacaoDispatcher.class.getName());
        previousLevel = logger.getLevel();
        previousUseParentHandlers = logger.getUseParentHandlers();

        handler = new CapturingHandler();
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    @AfterEach
    void tearDownLogger() {
        logger.removeHandler(handler);
        logger.setLevel(previousLevel);
        logger.setUseParentHandlers(previousUseParentHandlers);
    }

    @Test
    void shouldExecuteAllSupportedOperationsForOperacaoX() {
        when(operacaoXService.getTipo()).thenReturn(TipoOperacao.OPERACAO_X);
        var dispatcher = new OperacaoDispatcher(List.of(operacaoXService));
        var operacaoX = new Operacao(TipoOperacao.OPERACAO_X);

        dispatcher.executar(operacaoX);
        dispatcher.preExecutar(operacaoX);
        dispatcher.confirmar(operacaoX);

        verify(operacaoXService).executar(operacaoX);
        verify(operacaoXService).preExecutar(operacaoX);
        verify(operacaoXService).confirmar(operacaoX);
        verifyNoInteractions(operacaoYService);
    }

    @Test
    void shouldLogWhenOperationDoesNotSupportTheRequestedCapability() {
        when(operacaoYService.getTipo()).thenReturn(TipoOperacao.OPERACAO_Y);
        var dispatcher = new OperacaoDispatcher(List.of(operacaoYService));
        var operacaoY = new Operacao(TipoOperacao.OPERACAO_Y);

        clearInvocations(operacaoYService);
        dispatcher.executar(operacaoY);

        verifyNoInteractions(operacaoYService);
        assertThat(handler.messages())
                .anySatisfy(message -> assertThat(message).contains("não possui a funcionalidade 'executar'"));
    }

    @Test
    void shouldLogWhenNoServiceIsRegisteredForTheOperation() {
        var dispatcher = new OperacaoDispatcher(List.of());
        var operacaoZ = new Operacao(TipoOperacao.OPERACAO_Z);

        dispatcher.confirmar(operacaoZ);

        assertThat(handler.messages())
                .anySatisfy(message -> assertThat(message).contains("Nenhum serviço registrado"));
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
