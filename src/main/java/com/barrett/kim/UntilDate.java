package com.barrett.kim;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class UntilDate {

	private LocalDateTime until;
	private int previousCount;

	public UntilDate(LocalDateTime now) {
		this.until = now;
	}

	public void previousWeek() {
		until = until.minusWeeks(1);
		previousCount++;
	}

	public boolean isAfter(Date playedWhen) {
		return until.toInstant(ZoneOffset.UTC).isAfter(playedWhen.toInstant());
	}

	public int getWeekCount() {
		return previousCount;
	}

}
