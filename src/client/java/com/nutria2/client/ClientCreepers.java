package com.nutria2.client;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCreepers {
	private static final Set<UUID> SET = ConcurrentHashMap.newKeySet();

	public static void set(UUID id, boolean on) {
		if (on) SET.add(id); else SET.remove(id);
	}

	public static boolean is(UUID id) {
		return SET.contains(id);
	}

	public static void clear() {
		SET.clear();
	}
}
