package co.opensi.kkiapay_sdk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import co.opensi.kkiapay.KKiapayCallback;
import co.opensi.kkiapay.KkiaPay;
import co.opensi.kkiapay.STATUS;
import co.opensi.kkiapay.Subscriber;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by geecko on 6/4/18.
 */

public class MA extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KkiaPay manager = new KkiaPay("");

        Subscriber subscriber =  new Subscriber("22967434270","ALI","SHAD");

        manager.to(subscriber)
                .take(1500, new KKiapayCallback() {
                    @Override
                    public void onResponse(@NotNull STATUS status, @NotNull String phone) {

                    }
                });

    }
}
