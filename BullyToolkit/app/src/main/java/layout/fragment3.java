package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.exetxstate.bullytoolkit.R;
import com.exetxstate.bullytoolkit.util.IabHelper;
import com.exetxstate.bullytoolkit.util.IabResult;
import com.exetxstate.bullytoolkit.util.Inventory;
import com.exetxstate.bullytoolkit.util.Purchase;


public class fragment3 extends Fragment {

    private static final String TAG = "InAppBilling";
   // static final String ITEM_SKU = "android.test.purchased"; static response for early testing
    static final String ITEM_SKU = "com.example.buttonclick";
    IabHelper mHelper;

    private Button donateButton;
    private Button clickMe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment3, container, false);

        donateButton = (Button)myView.findViewById(R.id.donateButton);
        clickMe = (Button)myView.findViewById(R.id.clickMe);
        clickMe.setEnabled(false);

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwjlu2m+rhvvJKt1WxqvVSyL4gy8nQGmay/7pWS4M0ntMGHYES9kiecCBYCLsXriynCLnKf0ZN0p1uD" +
                        "Z5E2ehhbnjj1OdKej7XBaKWrw60/I+jum6gs0rIT+iUJi3LP9jEJHjr9P5tKFe2+8Fz3Hf4hBwdGAMiDdtwMeBLqwxRtnQb/" + "1vQojhaSjgOiw2ncS" +
                        "lAINkaJ/aGPzBmNFmBhXBEl50REruugZ8HSAihKe1Xz4w1GAzdmPzQxvSkohKTs+tdXQM9hnJ8BhM1n5TzNBbbWNqJGTQ1bWmOhibhXKUe/f4B31QfDOBFxJzp" +
                        "O1SlSZIvS0JI0Qa5AEtFOW5MyEuEQIDAQAB";

        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyClick(v);
            }
        });

        clickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });



        return myView;
    }

    public void buttonClicked(View view)
    {
        clickMe.setEnabled(false);
        donateButton.setEnabled(true);
    }

    public void buyClick(View view) {
        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001, mPurchaseFinishedListener, "mypurchasetoken");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // This method needs to identify if it was called bc in-app purchase or something else.
        if(!mHelper.handleActivityResult(requestCode, resultCode, data)) {      // It does this by calling the handleActivityResult method of the mHelper instance passing through
            super.onActivityResult(requestCode, resultCode, data);             // the incoming arguments. Notice how they are the same. If purchase, mHelper will Handle & return true
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener(){

        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if(result.isFailure()){
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)){
                consumeItem();                           //After making onIabPurchaseFinished.. we have to implement consumeItem();
                donateButton.setEnabled(false);

            }
        }
    };

    // Consuming the Purchased Item
    public void consumeItem() {
        mHelper.queryInventoryAsync(mRecievedInventoryListener);
    }


    IabHelper.QueryInventoryFinishedListener mRecievedInventoryListener = new IabHelper.QueryInventoryFinishedListener(){
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if(result.isFailure()){
                // Handle failure
            }

            else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if(result.isSuccess()) {
                clickMe.setEnabled(true);
            }
            else{
                // handle the error
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}
