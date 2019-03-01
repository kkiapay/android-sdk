package co.opensi.kkiapay_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import co.opensi.kkiapay.KKiapayCallback;
import co.opensi.kkiapay.MomoPay;
import co.opensi.kkiapay.STATUS;
import co.opensi.kkiapay.Subscriber;
import co.opensi.kkiapay.uikit.Kkiapay;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by geecko on 6/4/18.
 */

public class MA extends AppCompatActivity {

    private MomoPay manager;
    private Subscriber subscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = Kkiapay.INSTANCE.get().getMomoPay();
        subscriber =  new Subscriber("22997000000","FrstName","LastName");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Kkiapay.INSTANCE.get().setListener(new Function2<STATUS, String, Unit>() {
            @Override
            public Unit invoke(STATUS status, String s) {
                Toast.makeText(MA.this, "Transaction: ${status.name} -> $transactionId", Toast.LENGTH_LONG).show();
                return null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.momo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.from(subscriber)
                        .debit(1, new KKiapayCallback() {
                            @Override
                            public void onResponse(@NotNull STATUS status, @NotNull String phone, @NotNull String transactionId) {
                                Toast.makeText(MA.this, status.name() + " -> " + phone + " -> " + transactionId , Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });

        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de Kkiapay UI-kit paiement
                Kkiapay.INSTANCE.get().requestPayment(MA.this,
                        "1",
                        "Paiement de services",
                        "Nom Prenom", "");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Kkiapay.INSTANCE.get().handleActivityResult(requestCode, resultCode, data);
    }
}
