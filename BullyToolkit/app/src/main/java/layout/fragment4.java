package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.exetxstate.bullytoolkit.R;

public class fragment4 extends Fragment {

    View myView;
    private static Button disclaimButton;
    private static TextView disclaimerText;
    private static ViewAnimator viewAnimator;
    private static RelativeLayout fragment4Layout;
    private static ImageView bsod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_fragment4, container, false);
        init();
        viewAnimator.setAlpha(0);
        disclaimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAnimator.setAlpha(1);
                viewAnimator.showNext();
                fragment4Layout.setBackgroundResource(R.color.bsodblue);
                bsod.setVisibility(View.VISIBLE);
            }
        });

        return myView;
    }



    void init(){
        bsod = (ImageView) myView.findViewById(R.id.bsod);
        bsod.setVisibility(View.INVISIBLE);
        fragment4Layout = (RelativeLayout)myView.findViewById(R.id.fragment4Layout);
        disclaimButton = (Button)myView.findViewById(R.id.disclaimButton);
        disclaimerText = (TextView)myView.findViewById(R.id.disclaimerText);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bully.otf");
        disclaimButton.setTypeface(tf);
        viewAnimator = (ViewAnimator)myView.findViewById(R.id.viewAnimator);
        //Animation inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        Animation inAnim = new AlphaAnimation(0, 1);
        inAnim.setDuration(1000);

        viewAnimator.setInAnimation(inAnim);


    }


}