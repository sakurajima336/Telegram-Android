package com.wfc.wfgram;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

/**
 * Wfgram's own settings page, opened from the main Telegram settings list.
 * Every Wfgram feature gets a switch here; the values live in
 * {@link WfgramConfig}. Layout and toggle handling follow the upstream
 * settings activities (e.g. PrivacySettingsActivity).
 */
public class WfgramSettingsActivity extends BaseFragment {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CHECK = 1;
    private static final int TYPE_INFO = 2;

    private static final int ROW_FEATURES_HEADER = 0;
    private static final int ROW_NO_QUOTE_FORWARD = 1;
    private static final int ROW_NO_QUOTE_FORWARD_INFO = 2;
    private static final int ROW_ABOUT_HEADER = 3;
    private static final int ROW_ABOUT_INFO = 4;
    private static final int ROW_COUNT = 5;

    private RecyclerListView listView;
    private ListAdapter adapter;

    @Override
    protected boolean useSimpleActionBar() {
        return true;
    }

    @Override
    public View createView(Context context) {
        setHasOwnBackground(true);
        simpleActionBar.setBackButtonImage(R.drawable.ic_ab_back);
        simpleActionBar.setTitle(WfgramStrings.settingsTitle());
        simpleActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        FrameLayout frameLayout = new FrameLayout(context);
        fragmentView = frameLayout;

        listView = new RecyclerListView(context);
        listView.setSections();
        listView.setSectionsDrawBackground(true);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        listView.setVerticalScrollBarEnabled(false);
        listView.setLayoutAnimation(null);
        listView.setItemAnimator(null);
        listView.setAdapter(adapter = new ListAdapter(context));
        listView.setOnItemClickListener((view, position) -> {
            if (position == ROW_NO_QUOTE_FORWARD) {
                boolean newValue = !WfgramConfig.noQuoteForward();
                WfgramConfig.setNoQuoteForwardEnabled(newValue);
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(newValue);
                }
            }
        });
        frameLayout.addView(listView, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.MATCH_PARENT
        ));
        return fragmentView;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private final Context context;

        private ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return ROW_COUNT;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getAdapterPosition() == ROW_NO_QUOTE_FORWARD;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_HEADER) {
                view = new HeaderCell(context);
            } else if (viewType == TYPE_CHECK) {
                view = new TextCheckCell(context);
            } else {
                view = new TextInfoPrivacyCell(context);
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            ));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == TYPE_HEADER) {
                ((HeaderCell) holder.itemView).setText(
                        position == ROW_FEATURES_HEADER ? WfgramStrings.featuresHeader() : WfgramStrings.aboutHeader()
                );
            } else if (holder.getItemViewType() == TYPE_CHECK) {
                ((TextCheckCell) holder.itemView).setTextAndCheck(
                        WfgramStrings.noQuoteForward(),
                        WfgramConfig.noQuoteForward(),
                        true
                );
            } else {
                ((TextInfoPrivacyCell) holder.itemView).setText(
                        position == ROW_NO_QUOTE_FORWARD_INFO ? WfgramStrings.noQuoteForwardInfo() : WfgramStrings.aboutInfo()
                );
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == ROW_FEATURES_HEADER || position == ROW_ABOUT_HEADER) {
                return TYPE_HEADER;
            }
            if (position == ROW_NO_QUOTE_FORWARD) {
                return TYPE_CHECK;
            }
            return TYPE_INFO;
        }
    }

    @Override
    protected RecyclerListView getListViewForSimpleGlass() {
        return listView;
    }

    @Override
    public boolean isSupportEdgeToEdge() {
        return true;
    }
}
