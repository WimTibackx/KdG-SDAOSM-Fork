package be.kdg.groepa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Tim on 8/03/14.
 */
@Controller
@RequestMapping(value = "/language")
public class LanguageController extends BaseController{
    private Locale locale;
    private ResourceBundle bundle;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    void change(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
         if(data.equals("en")){
             locale = new Locale("nl");
             bundle = ResourceBundle.getBundle("i18n.nl");
         } else if(data.equals("nl")){
             locale = new Locale("en");
             bundle = ResourceBundle.getBundle("i18n.en");
         } else throw new UnsupportedOperationException("Language not supported");
    }

}
