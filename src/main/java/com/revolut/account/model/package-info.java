@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type = LocalDateTime.class, 
                        value = main.java.com.revolut.LocalDateTimeAdapter.class)
})
package  main.java.com.revolut.account.model;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;