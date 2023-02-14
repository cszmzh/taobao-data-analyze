package hashids.formatter;

import hashids.utils.HashidsUtils;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class HashidsFormatter implements Formatter<Long> {

    @Override
    public Long parse(String s, Locale locale) throws ParseException {
        return HashidsUtils.decode(s);
    }

    @Override
    public String print(Long id, Locale locale) {
        return HashidsUtils.encode(id);
    }
}
