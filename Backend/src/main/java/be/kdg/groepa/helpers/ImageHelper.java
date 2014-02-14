package be.kdg.groepa.helpers;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Tim on 14/02/14.
 */
public class ImageHelper {
    private static Random random = new Random();

    public static String writeUserImage(File picture, String name){
        return writeImage(picture, "src\\main\\webapp\\userImages\\"+name + random.nextInt());
    }

    public static String writeCarImage(File picture, String name){
        return writeImage(picture, "src\\main\\webapp\\carImages\\"+name + random.nextInt());
    }

    public static String editUserImage(File newPicture, String path){
        String fileName = path.substring(0, path.lastIndexOf('.'));
        File previous = new File(path);
        previous.delete();
        return writeImage(newPicture, fileName);
    }

    public static String writeImage(File picture, String name){
        String ext = picture.getName().substring(picture.getName().lastIndexOf("."), picture.getName().length());
        String filePath = name + ext;
        try {
            ImageIO.write(ImageIO.read(picture), ext.replace(".",""), new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }


    public static void removeUserImage(String imageUrl) {
        File file = new File(imageUrl);
        file.delete();
    }
}
