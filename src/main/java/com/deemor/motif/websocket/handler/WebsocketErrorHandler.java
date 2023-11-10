package com.deemor.motif.websocket.handler;

import com.deemor.motif.exception.BusinessException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.util.Objects;

public class WebsocketErrorHandler extends StompSubProtocolErrorHandler {

    @Builder
    @Getter
    public static class ErrorResponse {
        private HttpStatus httpStatus;
        private String message;
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable exception = ex;
        if (exception instanceof MessageDeliveryException) {
            exception = exception.getCause();
        }

        if (exception instanceof BusinessException) {
            return handleBusinessException(clientMessage, exception);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> handleBusinessException(Message<byte[]> clientMessage, Throwable ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());

        return prepareErrorMessage(clientMessage, apiError);

    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ErrorResponse errorResponse) {
        String message = errorResponse.getMessage();

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);


        setReceiptIdForClient(clientMessage, accessor);
        accessor.setMessage(message);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(), accessor.getMessageHeaders());
    }

    private void setReceiptIdForClient(final Message<byte[]> clientMessage, final StompHeaderAccessor accessor) {

        if (Objects.isNull(clientMessage)) {
            return;
        }

        final StompHeaderAccessor clientHeaderAccessor = MessageHeaderAccessor.getAccessor(
                clientMessage, StompHeaderAccessor.class);

        final String receiptId =
                Objects.isNull(clientHeaderAccessor) ? null : clientHeaderAccessor.getReceipt();

        if (receiptId != null) {
            accessor.setReceiptId(receiptId);
        }
    }
}
