package main.java.com.revolut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
	  @Override
	    public LocalDateTime unmarshal(String dateString) throws Exception {
	        Instant instant = Instant.parse(dateString);
	        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	        return dateTime;
	    }

	    @Override
	    public String marshal(LocalDateTime dateTime) throws Exception {
	        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
	        return DateTimeFormatter.ISO_INSTANT.format(instant);
	    }
}
