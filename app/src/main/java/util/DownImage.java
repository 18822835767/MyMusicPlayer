package util;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.net.URL;

import androidx.annotation.NonNull;

public class DownImage {
    private String image_path;
    
    public DownImage(String image_path){
        this.image_path = image_path;
    }
    
    public void loadImage(final ImageCallback imageCallback){
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Drawable drawable = (Drawable)msg.obj;
                imageCallback.getDrawable(drawable);
                return false;
            }
        });
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //第二个参数是 图片的名字，可用于debug，这里直接传入null
                    Drawable drawable = Drawable.createFromStream(new URL(image_path).openStream(),
                            null);
                    Message message = Message.obtain();
                    message.obj = drawable;
                    handler.sendMessage(message);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    @FunctionalInterface
    public interface ImageCallback{
        void getDrawable(Drawable drawable);
    }
}
