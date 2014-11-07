package br.diastecnologia.shopmaquinas.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {

	private static Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
	//private static Currency currency = Currency.getInstance(locale);
	private static NumberFormat format =  NumberFormat.getCurrencyInstance(locale);
	
	public static String toString( double amount ){
		return format.format(amount);
	}
}
