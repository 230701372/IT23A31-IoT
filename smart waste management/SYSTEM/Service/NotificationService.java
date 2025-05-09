package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.DTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyDriver(String driverId, NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/driver/" + driverId, message);
    }
}
