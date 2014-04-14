/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.montanafoodhub.Helena_Hub.R;

public class ActionBar extends RelativeLayout implements View.OnClickListener {

    private static final String LogTag = ActionBar.class.getSimpleName();

    public enum Action {
        Unknown,
        Left,
        Center,
        Right
    }

    public interface ActionBarClickListener {
        public void onActionClicked(Action action);
    }


    private ActionBarClickListener _clickListener;

    public void setOnClickActionListener(ActionBarClickListener listener) {
        _clickListener = listener;
    }

    public void setLeftActionText(String text) {
        setText(R.id.leftActionLayout, text);
    }

    public void setLeftActionImage(Drawable image) {
        setImage(R.id.leftActionLayout, image);
    }

    public void setCenterActionText(String text) {
        setText(R.id.centerActionLayout, text);
    }

    public void setCenterActionImage(Drawable image) {
        setImage(R.id.centerActionLayout, image);
    }

    public void setRightActionText(String text) {
        setText(R.id.rightActionLayout, text);
    }

    public void setRightActionImage(Drawable image) {
        setImage(R.id.rightActionLayout, image);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode() == false) {
            View.inflate(context, R.layout.control_action_bar, this);

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar, 0, 0);
            setupAction(a, R.id.leftActionLayout, R.styleable.ActionBar_leftActionText, R.styleable.ActionBar_leftActionImage);
            setupAction(a, R.id.centerActionLayout, R.styleable.ActionBar_centerActionText, R.styleable.ActionBar_centerActionImage);
            setupAction(a, R.id.rightActionLayout, R.styleable.ActionBar_rightActionText, R.styleable.ActionBar_rightActionImage);
            a.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        if (_clickListener != null) {
            Action action = Action.Unknown;

            if (v.getId() == R.id.leftActionLayout) {
                action = Action.Left;
            }
            else if (v.getId() == R.id.centerActionLayout) {
                action = Action.Center;
            }
            else if (v.getId() == R.id.rightActionLayout) {
                action = Action.Right;
            }

            _clickListener.onActionClicked(action);
        }
    }

    private void setupAction(TypedArray attributes, int actionLayoutId, int textAttr, int imageAttr) {
        View actionContainer = findViewById(actionLayoutId);
        actionContainer.setOnClickListener(this);

        String text = attributes.getString(textAttr);
        TextView textView = (TextView) actionContainer.findViewById(R.id.textView);
        textView.setText(text);

        Drawable image = attributes.getDrawable(imageAttr);
        ImageView imageView = (ImageView) actionContainer.findViewById(R.id.imageView);
        imageView.setImageDrawable(image);
    }

    private void setText(int layoutId, String text) {
        View layout = findViewById(layoutId);
        TextView textView = (TextView) layout.findViewById(R.id.textView);
        textView.setText(text);
    }

    private void setImage(int layoutId, Drawable image) {
        View layout = findViewById(layoutId);
        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
        imageView.setImageDrawable(image);
    }
}
