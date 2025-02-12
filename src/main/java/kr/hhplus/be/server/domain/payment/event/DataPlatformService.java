package kr.hhplus.be.server.domain.payment.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataPlatformService {

    public void sendMessage(String message) {
        try {
            log.info("Successful payment transfer to the data platform");
        } catch(IllegalStateException e) {
            log.error("Failed to send payment to data platform", e);
        }
    }
}
