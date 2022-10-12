package co.opensi.kkiapay_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import co.opensi.kkiapay.KKiapayCallback;
import co.opensi.kkiapay.MomoPay;
import co.opensi.kkiapay.STATUS;
import co.opensi.kkiapay.Subscriber;
import co.opensi.kkiapay.uikit.Kkiapay;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        //setContentView(R.layout.activity_main);

        Button testButtonWithKkiapay = (Button) findViewById(R.id.test_button_with_kkiapay);
        testButtonWithKkiapay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Kkiapay.get().requestPayment(MainActivity.this,
                        1,
                        "Paiement de services",
                        "Nom Prenom", "");
            }
        });

        Button testButtonWithoutKkiapay = (Button) findViewById(R.id.test_button_without_kkiapay);
        testButtonWithoutKkiapay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Subscriber subscriber = new Subscriber ("22961XXXX82", "DOE", "Johna","");
                MomoPay manager = Kkiapay.get().getMomoPay ();
                manager.from(subscriber).debit (1,
                        new KKiapayCallback () {
                            @Override
                            public void onResponse (@NonNull STATUS status, @NonNull String phone, @NonNull String transactionId) {
                                switch (status) {

                                    case FAILED :
                                        Toast.makeText(MainActivity.this, "Transaction: FAILED -> $transactionId", Toast.LENGTH_LONG).show();
                                        break;
                                    case SUCCESS :
                                        Toast.makeText(MainActivity.this, "Transaction: SUCCESS -> $transactionId", Toast.LENGTH_LONG).show();
                                        break;
                                    case INSUFFICIENT_FUND :
                                        //...
                                        break;
                                }

                            }
                        }

                );
            }
        });
    }

    @Override
    protected void onStart () {
        super.onStart ();
        // Setup listener for payment status
        Kkiapay.get().setListener(new Function2<STATUS, String, Unit> () {
            @Override
            public Unit invoke(STATUS status, String s) {
                //The following code will be run when user will end the payment
                Toast.makeText(MainActivity.this, "Transaction: ${status.name} -> $transactionId", Toast.LENGTH_LONG).show();
                return null;
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        Kkiapay.get().handleActivityResult(requestCode, resultCode, data);
    }
}
