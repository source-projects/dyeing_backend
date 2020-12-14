package com.main.glory.schedulers;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class GCScheduler {

	@Scheduled(fixedDelay = 20000)
	public void cleanGarbage(){
		System.gc();
	}
}
