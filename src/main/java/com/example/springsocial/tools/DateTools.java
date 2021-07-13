/**
 * 
 */
package com.example.springsocial.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONObject;

/**
 * @author jorge
 *
 */
public class DateTools<T> {
	private Date myDate = new Date();
	private SimpleDateFormat _dateTimeSpanishFormat = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
	private SimpleDateFormat _dateSpanishFormat = new SimpleDateFormat ("dd-MM-yyyy");
	private SimpleDateFormat _dateSpanishFormat2 = new SimpleDateFormat ("dd/MM/yyyy hh24:mm:ss");
	private SimpleDateFormat ISO8601DateFormat= new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	
	public Date get_CurrentDate(){
		return new Date();
	}
	
	@SuppressWarnings("unchecked")
	public T getDateTimeSpanishFormat(Date date){
		if (date==null){
			return null;
		}
		return (T) (_dateTimeSpanishFormat.format(date));
	}
	
	@SuppressWarnings("unchecked")
	public T getDateSpanishFormat(Date date){
		if (date==null){
			return null;
		}
		return (T) (_dateSpanishFormat.format(date));
	}
	
	public Date convertStringToDate(String date) throws ParseException {
		return _dateSpanishFormat.parse(date);
	}
	
	public Date convertStringToDate2(String date) throws ParseException {
		return _dateSpanishFormat2.parse(date);
	}
	
	public Date convertISO8601StringToDate(String date) throws ParseException {
		ISO8601DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return ISO8601DateFormat.parse(date.toString().toUpperCase());
        
	}
	
	public JSONObject getHoraActual() {
		LocalDateTime locaDate = LocalDateTime.now();
		JSONObject horas = new JSONObject();
		Integer hora  = locaDate.getHour();
		
		horas.put("horaInicial",formatohora(hora));
		horas.put("horaFinal", formatohora(hora+2));
		
		return horas;
	}
	
	public JSONObject getHorasConsumo(Integer hora) {
		JSONObject horas = new JSONObject();
		
		horas.put("horaInicial", formatohora(hora));
		horas.put("horaFinal", formatohora(hora+2));
		return horas;
	}
	
	public String formatohora(Integer hora) {
		String horaFormato = null;
		if(hora>=10) {
			horaFormato = ""+hora+":00:00";
		}else {
			horaFormato = "0"+hora+":00:00";
		}
		
		return horaFormato;
	}
	
	public Date restar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}
	
	public Date convertAndgetDate(String fecha){
		JSONObject resultado = new JSONObject();		
		 resultado = Id(fecha);
		
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(0);
		 cal.set(Integer.valueOf(resultado.get("ano").toString()),Integer.valueOf(resultado.get("mes").toString())-1 ,Integer.valueOf(resultado.get("dia").toString()), 0,0,0);
		 Date date = cal.getTime(); // get back a Date object
		 return date;
	 }
	
	private JSONObject Id(String idpaquete) {		
		String ano="", mes="", dia="";
		JSONObject objfecha = new JSONObject();
		//15/08/2018
		char regla1 = '/';
		char regla2 = '-';
		for(int i=0;i<idpaquete.length();i++) {
			
			char c = idpaquete.charAt(i);
			if(Character.compare(regla1, c)<0 || Character.compare(regla2, c)<0) {
				if(i<2) {
					dia = dia + c;
				}
				if(i>2 && i<5) {
					mes = mes + c;
				}
				if(i>5) {
					ano = ano + c;
				}
			}
		}
		
		dia = dia.replaceFirst ("^0*", "");
		mes = mes.replaceFirst("^0*", "");
		ano = ano.replaceFirst("^0*", "");
		
		objfecha.put("dia",dia);
		objfecha.put("mes", mes);
		objfecha.put("ano", ano);		
		return objfecha;
	}
	
	public Date getLastDateOfMonth(Date date){
		//return getLastDateOfMonth(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	 }
	 
	 public Date getLastDateOfCurrentDate(){
		 return getLastDateOfMonth(new Date());
	 }
	 
	 public int getLastDayOfCurrentDate(){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(new Date());
		 return  cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 }
	
	 public int getDayOfCurrentDate(){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(new Date());
		 return cal.get(Calendar.DAY_OF_MONTH);
	 }
	 
	 public int getDayOfCurrentDate(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 return cal.get(Calendar.DAY_OF_MONTH);
	 }
	 
	 public int getMonthOfCurrentDate(){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(new Date());
		 return cal.get(Calendar.MONTH)+1;
	 }

	 public int getMonthOfCurrentDate(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 return cal.get(Calendar.MONTH)+1;
	 }

	 public int getYearOfCurrentDate(){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(new Date());
		 return cal.get(Calendar.YEAR);
	 }
	 
	 public int getYearOfCurrentDate(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 return cal.get(Calendar.YEAR);
	 }

	 
	 public Date getDate(int ano, int mes){
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(0);
		 cal.set(ano, mes, 1, 0,0,0);
		 Date date = cal.getTime(); // get back a Date object
		 return date;
	 }
	 
	 
	  public long countBusinessDaysBetween(LocalDate startDate, LocalDate endDate,Optional<List<LocalDate>> holidays) {
	        if (startDate == null || endDate == null || holidays == null) {
	            throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween(" + startDate
	                    + "," + endDate + "," + holidays + ")");
	        }
	 
	        Predicate<LocalDate> isHoliday = date -> holidays.isPresent() ? holidays.get().contains(date) : false;
	 
	        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
	                || date.getDayOfWeek() == DayOfWeek.SUNDAY;
	 
	        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
	 
	        long businessDays = Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween+1)
	                .filter(isHoliday.or(isWeekend).negate())
	        		.count();
	        
	        return businessDays;
	    }
	  
	  public Date addBusinessDaysToDate(Date startDate, Integer daysNumber) {
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(startDate);
	      
		  int index = 0;
		  while (index < daysNumber) {
				calendar.add(Calendar.DATE, 1);
				//System.out.println("day "+calendar.get(Calendar.DAY_OF_WEEK)+"- "+calendar.getTime());
				if (!(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY))
					index++;
		  }
		  return calendar.getTime();
	  }
	  
	  public long getDifferenceDays(Date startDate, Date endDate) {
		    long diff = endDate.getTime() - startDate.getTime();
		    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	  }
	  
	  public Date addHoursToDate(Date initDate, Integer hours) {
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(initDate); 
		  cal.add(Calendar.HOUR_OF_DAY, hours);
		  return cal.getTime();  
	  }
	  
	  public Date addDaysToDate(Date initDate, Integer days) {
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(initDate); 
		  cal.add(Calendar.DAY_OF_MONTH, days);
		  return cal.getTime();  
	  }
	  
	  public static void main(String[] args) throws ParseException {
		  try {
			  /*LocalDate startDate= LocalDate.of(2020,5,9);
			  LocalDate endDate= LocalDate.of(2020,5,18);
			  
			  long days= countBusinessDaysBetween(startDate,endDate,Optional.empty());
			  System.out.println("dias: "+days);
			  */
		  } catch(Exception ex) {
			  System.out.print("ex"+ex.toString());
		  }
	 }
}
