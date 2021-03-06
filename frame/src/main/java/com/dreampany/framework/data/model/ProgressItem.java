package com.dreampany.framework.data.model;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreampany.framework.R;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.Payload;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by air on 10/19/17.
 */

public class ProgressItem extends AbstractFlexibleItem<ProgressItem.ProgressViewHolder> {

    private StatusEnum status = StatusEnum.MORE_TO_LOAD;

    @Override
    public boolean equals(Object inObject) {
        return this == inObject;//The default implementation
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_progress;
    }

    @Override
    public ProgressViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ProgressViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ProgressViewHolder holder,
                               int position, List payloads) {

        Context context = holder.itemView.getContext();
        holder.progressBar.setVisibility(View.GONE);
        holder.viewMessage.setVisibility(View.VISIBLE);

        if (!adapter.isEndlessScrollEnabled()) {
            setStatus(StatusEnum.DISABLE_ENDLESS);
        } else if (payloads.contains(Payload.NO_MORE_LOAD)) {
            setStatus(StatusEnum.NO_MORE_LOAD);
        }

        switch (this.status) {
            case NO_MORE_LOAD:
                holder.viewMessage.setText(
                        context.getString(R.string.no_more_load_retry));
                // Reset to default status for next binding
                setStatus(StatusEnum.MORE_TO_LOAD);
                break;
            case DISABLE_ENDLESS:
                holder.viewMessage.setText(context.getString(R.string.endless_disabled));
                break;
            case ON_CANCEL:
                holder.viewMessage.setText(context.getString(R.string.endless_cancel));
                // Reset to default status for next binding
                setStatus(StatusEnum.MORE_TO_LOAD);
                break;
            case ON_ERROR:
                holder.viewMessage.setText(context.getString(R.string.endless_error));
                // Reset to default status for next binding
                setStatus(StatusEnum.MORE_TO_LOAD);
                break;
            default:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.viewMessage.setVisibility(View.GONE);
                break;
        }
    }

    static class ProgressViewHolder extends FlexibleViewHolder {

        ProgressBar progressBar;
        TextView viewMessage;

        ProgressViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            progressBar = view.findViewById(R.id.progressBar);
            viewMessage = view.findViewById(R.id.viewMessage);
        }

        @Override
        public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {
            AnimatorHelper.scaleAnimator(animators, itemView, 0f);
        }
    }

    public enum StatusEnum {
        MORE_TO_LOAD, //Default = should have an empty Payload
        DISABLE_ENDLESS, //Endless is disabled because user has set limits
        NO_MORE_LOAD, //Non-empty Payload = Payload.NO_MORE_LOAD
        ON_CANCEL,
        ON_ERROR
    }
}
