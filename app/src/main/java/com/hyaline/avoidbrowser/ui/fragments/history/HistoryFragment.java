package com.hyaline.avoidbrowser.ui.fragments.history;

import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.VibrateUtils;
import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseFragment;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.beans.BrowseHistoryBean;
import com.hyaline.avoidbrowser.data.beans.CollectBean;
import com.hyaline.avoidbrowser.databinding.FragmentHistoryBinding;
import com.hyaline.avoidbrowser.ui.activities.main.MainActivity;
import com.hyaline.avoidbrowser.ui.fragments.history.data.SectionHeader;
import com.hyaline.avoidbrowser.ui.fragments.history.data.SectionItem;
import com.hyaline.avoidbrowser.utils.ClipboardUtils;
import com.hyaline.avoidbrowser.utils.ThreadPool;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.qmuiteam.qmui.widget.section.QMUISection;
import com.qmuiteam.qmui.widget.section.QMUIStickySectionAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends BaseFragment<HistoryViewModel, FragmentHistoryBinding> implements QMUIQuickAction.OnClickListener {
    private HistorySectionAdapter adapter;
    private QMUIQuickAction longClickAction;
    private SectionItem sectionItem;

    @Override
    protected int layoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initHistories();
    }

    private void initHistories() {
        adapter = new HistorySectionAdapter(getContext());
        adapter.setCallback(new QMUIStickySectionAdapter.Callback<SectionHeader, SectionItem>() {
            @Override
            public void loadMore(QMUISection<SectionHeader, SectionItem> section, boolean loadMoreBefore) {

            }

            @Override
            public void onItemClick(QMUIStickySectionAdapter.ViewHolder holder, int position) {
                if (holder instanceof ItemViewHolder) {
                    String url = ((ItemViewHolder) holder).getItem().item.getUrl();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("extra", "fromHistory");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(QMUIStickySectionAdapter.ViewHolder holder, int position) {
                VibrateUtils.vibrate(10);
                if (holder instanceof ItemViewHolder) {
                    sectionItem = ((ItemViewHolder) holder).getItem();
                    checkPopup();
                    longClickAction.show(holder.itemView);
                }
                return false;
            }
        });
        dataBinding.sectionView.getStickySectionWrapView().setBackgroundColor(getResources().getColor(R.color.app_start, null));
        dataBinding.sectionView.setAdapter(adapter);
        dataBinding.sectionView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getDao().loadLiveHistoriesBytime(50).observe(this, browseHistoryBeans -> {
            Map<Long, List<SectionItem>> map = new LinkedHashMap<>();
            for (BrowseHistoryBean historyBean : browseHistoryBeans) {
                Long dayStart = getDayStart(historyBean.getTime());
                if (map.containsKey(dayStart)) {
                    map.get(dayStart).add(new SectionItem(historyBean));
                } else {
                    List<SectionItem> list = new ArrayList<>();
                    list.add(new SectionItem(historyBean));
                    map.put(dayStart, list);
                }
            }
            List<QMUISection<SectionHeader, SectionItem>> list = new ArrayList<>();
            for (Map.Entry<Long, List<SectionItem>> entry : map.entrySet()) {
                SectionHeader header = new SectionHeader(entry.getKey());
                QMUISection<SectionHeader, SectionItem> section = new QMUISection<>(header, entry.getValue(), false);
                list.add(section);
            }
            adapter.setData(list);
        });
    }

    private void checkPopup() {
        if (longClickAction == null) {
            longClickAction = QMUIPopups.quickAction(getContext(), QMUIDisplayHelper.dp2px(getContext(), 56),
                    QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 10))
                    .bgColor(0xFFFFE4B5)
                    .onDismiss(() -> sectionItem = null)
                    .addAction(newAction(R.drawable.copy, "复制", this))
                    .addAction(newAction(R.drawable.star, "收藏", this))
                    .addAction(newAction(R.drawable.delete, "删除", this));
        }
    }

    private Calendar cal;

    private Long getDayStart(long time) {
        if (cal == null) {
            cal = Calendar.getInstance();
        }
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private QMUIQuickAction.Action newAction(int icon, String text, QMUIQuickAction.OnClickListener listener) {
        return new QMUIQuickAction.Action().icon(icon).text(text).onClick(listener);
    }

    @Override
    public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
        if (sectionItem == null) {
            return;
        }
        switch (position) {
            case 0:
                ClipboardUtils.copyText(sectionItem.item.getUrl());
                Toast.makeText(getContext(), "已成功复制地址到剪切板~", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                ThreadPool.fixed().execute(() -> {
                    AppDatabase database = AppDatabase.getDatabase();
                    CollectBean exist = database.collectDao().exist(sectionItem.item.getUrl());
                    if (exist == null) {
                        CollectBean bean = new CollectBean();
                        bean.setName(sectionItem.item.getTitle());
                        bean.setUrl(sectionItem.item.getUrl());
                        database.collectDao().insert(bean);
                        ThreadPool.main().execute(() -> Toast.makeText(getContext(), "已收藏该网址~", Toast.LENGTH_SHORT).show());
                    } else {
                        ThreadPool.main().execute(() -> Toast.makeText(getContext(), "该网址已经收藏！", Toast.LENGTH_SHORT).show());
                    }
                });
                break;
            case 2:
                ThreadPool.fixed().execute(() -> viewModel.getDao().delete(sectionItem.item));
                break;
        }
        quickAction.dismiss();
    }

}
