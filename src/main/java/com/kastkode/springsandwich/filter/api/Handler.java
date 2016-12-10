package com.kastkode.springsandwich.filter.api;

import java.util.function.BiPredicate;

/*
 * a common super interface.  don't use directly, instead choose one of its subtypes, eg BeforeHandler or AfterHandler
 */
public interface Handler {
	
	public default boolean hasFlag(String[] flags, String flag) {		
		return hasFlag(flags, flag, 
			(s1, s2) -> s1.equals(s2) 
		);
	}
	
	public default boolean hasFlagIgnoreCase(String[] flags, String flag) {		
		return hasFlag(flags, flag, 
			(s1, s2) -> s1.equalsIgnoreCase(s2) 
		);
	}
	
	public default boolean hasFlag(String[] flags, String flag, BiPredicate<String, String> predicate) {
		if(flags == null || flag == null) return false;
		
		for(int i=0; i<flags.length; i++) {
			String f = flags[0];
			if(f != null && predicate.test(f, flag)) {
				return true;
			}
		}
		return false;		
	}
}
