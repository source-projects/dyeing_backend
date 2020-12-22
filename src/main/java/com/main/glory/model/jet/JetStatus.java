package com.main.glory.model.jet;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum JetStatus {
    success,onProcess,inQueue,inProgress,planned,pause,emergencyStop;
}
