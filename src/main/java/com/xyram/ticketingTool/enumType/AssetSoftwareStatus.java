package com.xyram.ticketingTool.enumType;

import java.util.Arrays;
import java.util.Optional;

public enum AssetSoftwareStatus {

	ACTIVE("ACTIVE"),
    INSTALL("INSTALL"),
    UNINSTALL("UNINSTALL"),
	INACTIVE("INACTIVE");
	private String value;

	AssetSoftwareStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static AssetSoftwareStatus toEnum(String value) {
		Optional<AssetSoftwareStatus> optional = Arrays.stream(values()).filter(e -> e.value.equalsIgnoreCase(value))
				.findFirst();
		if (optional.isPresent())
			return optional.get();
		else
			throw new IllegalArgumentException(value + " is not a valid status");
	}
}
