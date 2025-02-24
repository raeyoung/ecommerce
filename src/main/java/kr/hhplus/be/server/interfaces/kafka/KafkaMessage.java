package kr.hhplus.be.server.interfaces.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KafkaMessage {

    private String name;

    private String message;
}
