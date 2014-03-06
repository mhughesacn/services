package org.collectionspace.services.structureddate;

import org.joda.time.DateTime;
import org.joda.time.chrono.GJChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
	private static DateTimeFormatter monthFormatter = DateTimeFormat.forPattern("MMMM");
	
	public static int getMonthByName(String monthName) {
		return monthFormatter.parseDateTime(monthName).getMonthOfYear();
	}
	
	public static int getDaysInMonth(int month, int year) {
		DateTime dateTime = new DateTime(GJChronology.getInstance()).withYear(year).withMonthOfYear(month);
		
		return dateTime.dayOfMonth().getMaximumValue();
	}
	
	public static Date getQuarterYearStartDate(int year, int quarter) {
		int startMonth = getQuarterYearStartMonth(quarter);
		
		return new Date(year, startMonth, 1);
	}
	
	public static Date getQuarterYearEndDate(int year, int quarter) {
		int endMonth = getQuarterYearEndMonth(quarter);
		
		return new Date(year, endMonth, DateUtils.getDaysInMonth(endMonth, year));
	}
	
	public static int getQuarterYearStartMonth(int quarter) {
		return ((3 * (quarter-1)) + 1);
	}
	
	public static int getQuarterYearEndMonth(int quarter) {
		return (getQuarterYearStartMonth(quarter) + 2);
	}
	
	public static Date getHalfYearStartDate(int year, int half) {
		int startMonth = getHalfYearStartMonth(half);
		
		return new Date(year, startMonth, 1);
	}

	public static Date getHalfYearEndDate(int year, int half) {
		int endMonth = getHalfYearEndMonth(half);
		
		return new Date(year, endMonth, DateUtils.getDaysInMonth(endMonth, year));
	}

	public static int getHalfYearStartMonth(int half) {
		return ((6 * (half-1)) + 1);
	}
	
	public static int getHalfYearEndMonth(int half) {
		return (getHalfYearStartMonth(half) + 5);
	}
	
	public static int getDecadeStartYear(int year, Era era) {
		if (era == null) {
			era = Date.DEFAULT_ERA;
		}
		
		int startYear;
		
		if (era == Era.BCE) {
			startYear = year + 9;
		}
		else {
			startYear = year;
		}
		
		return startYear;
	}
	
	public static int getDecadeEndYear(int year, Era era) {
		if (era == null) {
			era = Date.DEFAULT_ERA;
		}
		
		int endYear;
		
		if (era == Era.BCE) {
			endYear = year;
		}
		else {
			endYear = year + 9;			
		}
		
		return endYear;
	}
	
	public static int getCenturyStartYear(int year, Era era) {
		if (era == null) {
			era = Date.DEFAULT_ERA;
		}
		
		int startYear;
		
		if (era == Era.BCE) {
			startYear = year + 99;
		}
		else {
			startYear = year;
		}
		
		return startYear;
	}
	
	public static int getCenturyEndYear(int year, Era era) {
		if (era == null) {
			era = Date.DEFAULT_ERA;
		}
		
		int endYear;
		
		if (era == Era.BCE) {
			endYear = year;
		}
		else {
			endYear = year + 99;			
		}
		
		return endYear;
	}
	
	/**
	 * Calculates the interval, in years, that should be padded around a date so
	 * that any date within that interval may be considered to be "circa" the
	 * given date. 
	 * 
	 * @param  year The year of the date
	 * @param  era  The era of the date. If null, Date.DEFAULT_ERA is assumed.
	 * @return      The number of "circa" years before and after the date  
	 */
	public static int getCircaIntervalYears(int year, Era era) {
		/*
		 * This algorithm is inherited from the fuzzydate parser
		 * in XDB. Its comment states:
		 * 
		 *   We define circa year/century specifications offsets
 		 *   as +/- 5% of the difference between that year/century
		 *   and the present (2100), so that the farther we go back
		 *   in time, the wider the range of meaning of "circa."
		 *    
		 */
		
		if (era == null) {
			era = Date.DEFAULT_ERA;
		}
		
		if (era == Era.BCE) {
			year = -year;
		}
	
		return ((int) Math.round(Math.abs(2100 - year) * 0.05));
	}
	
	/**
	 * Adds a number of years to a date.
	 * 
	 * @param date  The date	
	 * @param years The number of years to add to the date
	 */
	public static void addYears(Date date, int years) {
		Integer currentYear = date.getYear();
		
		if (currentYear == null) {
			throw new IllegalArgumentException("null year in date");
		}

		Era currentEra = date.getEra();
		
		if (currentEra == null) {
			currentEra = Date.DEFAULT_ERA;
		}

		int newYear = currentYear;
		Era newEra = currentEra;

		if (years > 0) {
			if (currentEra == Era.CE) {
				newYear = currentYear + years;
			}
			else {
				newYear = currentYear - years;
				
				if (newYear <= 0) {
					// We crossed the BC/AD boundary. Flip the sign, and 
					// add one, since there's no year zero.					
					newYear = -newYear + 1;
					
					// Change the era.
					newEra = Era.CE;
				}
			}
		}
		else if (years < 0) {
			years = -years;
			
			if (currentEra == Era.BCE) {
				newYear = currentYear + years;
			}
			else {
				newYear = currentYear - years;
				
				if (newYear <= 0) {
					// We crossed the AD/BC boundary. Flip the sign, and
					// add one, since there's no year zero.
					newYear = -newYear + 1;
					
					// Change the era.
					newEra = Era.BCE;
				}
			}
		}
		
		date.setYear(newYear);
		date.setEra(newEra);
	}
	
	/**
	 * Subtracts a number of years from a date.
	 * 
	 * @param date  The date	
	 * @param years The number of years to subtract from the date
	 */
	public static void subtractYears(Date date, int years) {
		addYears(date, -years);
	}
}