package com.main.glory.model.jet;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum JetStatus {
    success,onProcess,inQueue,planned,pause,emergencyStop;
}
