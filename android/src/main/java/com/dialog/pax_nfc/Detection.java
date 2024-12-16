package com.dialog.pax_nfc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.pax.dal.IDAL;
import com.pax.dal.IPicc;
import com.pax.dal.entity.ApduSendInfo;
import com.pax.dal.entity.EBeepMode;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccRemoveMode;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.PiccCardInfo;
import com.pax.dal.exceptions.EPiccDevException;
import com.pax.dal.exceptions.PiccDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

import util.Convert;
import util.IApdu;
import util.Packer;

public class Detection {

    private static IPicc picc;
    private static IDAL dal;
    private static Context appContext;
    private static EPiccType piccType = EPiccType.INTERNAL;

    public static void setUp(Context ctx){
        appContext =  ctx;
        dal = getDal();
        picc = dal.getPicc(piccType);
    }

    public static IDAL getDal(){
        if(dal == null){
            try {
                long start = System.currentTimeMillis();
                dal = NeptuneLiteUser.getInstance().getDal(appContext);
                Log.i("Test","get dal cost:"+(System.currentTimeMillis() - start)+" ms");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(appContext, "error occurred,DAL is null.", Toast.LENGTH_LONG).show();
            }
        }
        return dal;
    }

    public static PiccCardInfo detect(EDetectMode mode) {
        try {
            PiccCardInfo cardInfo = picc.detect(mode);
            return cardInfo;
        } catch (PiccDevException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int detectType(Handler handler, EDetectMode detectMode) {

        PiccCardInfo cardInfo;
        if (null != (cardInfo = detect(detectMode))) {

            Message message = Message.obtain();
            message.what = 0;
            message.obj = Convert.getInstance().bcdToStr(
                    (cardInfo.getSerialInfo() == null) ? "".getBytes() : cardInfo.getSerialInfo());
            handler.sendMessage(message);

            return 1;
        } else {

            return 0;
        }

    }
}
