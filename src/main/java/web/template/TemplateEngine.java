package web.template;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TemplateEngine {
    public String process(String text){
        text = replaceVariables(text);
        return text;
    }

    private String replaceVariables(String text){
        Date date = new Date();
        String today = new SimpleDateFormat("hh:mm:ss - YYYY:MM:DD").format(date);

        text = text.replace("${today}", today);
        return text;
    }
}
