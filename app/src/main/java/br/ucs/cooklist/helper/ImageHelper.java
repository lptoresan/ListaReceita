package br.ucs.cooklist.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;

public class ImageHelper {

    /**
     * Converte uma base64 para um {@link Bitmap}.
     *
     * @param base64
     * @return
     */
    public static Bitmap fromBase64(String base64) {
        byte[] decodedByte = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        return bitmap;
    }

    /**
     * Converte um {@link Bitmap} para base64.
     *
     * @param img
     * @return
     */
    public static String toBase64(Bitmap img) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void loadToImageView(Context context, String base64, ImageView imgView) {
        Bitmap img = ImageHelper.fromBase64(base64);
        loadToImageView(context, img, imgView);
    }

    public static void loadToImageView(Context context, Bitmap img, ImageView imgView) {
        Glide.with(context)//
                .load(img)//
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(20)))//
                .into(imgView);
    }


}
