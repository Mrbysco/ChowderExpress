package com.mrbysco.chowderexpress;

import com.mrbysco.chowderexpress.registration.CartRegistry;

public class CommonClass {

	public static void init() {
		CartRegistry.loadClass();
	}
}