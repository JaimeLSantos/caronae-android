package br.ufrj.caronae.customizedviews;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;

import android.widget.LinearLayout;

import br.ufrj.caronae.R;

public class CustomBottomDialogClass extends Dialog implements View.OnClickListener {

    private Activity activity;
    private Fragment fragment;
    private String currentFrag;
    private LinearLayout cancel_bt, remove_bt;

    public CustomBottomDialogClass(Activity activity, String currentFrag, Fragment fragment) {
        super(activity);
        this.activity = activity;
        this.fragment = fragment;
        this.currentFrag = currentFrag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_bottom_dialog);
        this.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        cancel_bt = findViewById(R.id.cancel_option);
        remove_bt = findViewById(R.id.remove_option);
        cancel_bt.setOnClickListener(this);
        remove_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.cancel_option:
                dismiss();
                break;

            case R.id.remove_option:
                dismiss();
                break;

            default:

                break;
        }
    }
}