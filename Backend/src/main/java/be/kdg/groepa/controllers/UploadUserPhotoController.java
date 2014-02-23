package be.kdg.groepa.controllers;

import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.exceptions.CarNotOfUserException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by delltvgateway on 2/21/14.
 */
@Controller
public class UploadUserPhotoController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/authorized/user/uploadphoto",method= RequestMethod.POST)
    public @ResponseBody String uploadPhoto(@RequestParam("file") MultipartFile photo, HttpServletRequest request, HttpServletResponse response) {
        User user = super.getCurrentUser(request);

        InputStream is = null;
        try {
            is = photo.getInputStream();
            userService.setUserPicture(user, is, photo.getOriginalFilename());
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return super.respondSimpleAuthorized("error", "ImageError", request, response);
        }

        return super.respondSimpleAuthorized("url", user.getAvatarURL(), request, response);
    }

    @RequestMapping(value="/authorized/user/deletephoto", method=RequestMethod.POST)
    public @ResponseBody String deletePhoto(HttpServletRequest request, HttpServletResponse response) {
        User user = super.getCurrentUser(request);

        userService.removeUserPicture(user);
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }
}
