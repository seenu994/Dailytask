package com.xyram.ticketingTool.enumType;

import java.util.Arrays;
import java.util.Optional;

public enum JobOpeningStatus {
	VACATE("VACATE"),

	COMPLETED("COMPLETED"),
	
	CANCELLED("CANCELLED");
	private String value;

	JobOpeningStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static JobOpeningStatus toEnum(String value) {
		Optional<JobOpeningStatus> optional = Arrays.stream(values()).filter(e -> e.value.equalsIgnoreCase(value))
				.findFirst();
		if (optional.isPresent())
			return optional.get();
		else
			throw new IllegalArgumentException(value + " is not a valid status");
	}
}
