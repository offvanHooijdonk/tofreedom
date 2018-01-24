package by.offvanhooijdonk.tofreedom.ui.stories;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.app.ToFreedomApp;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.ResHelper;
import by.offvanhooijdonk.tofreedom.model.StoryModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PastStoriesActivity extends AppCompatActivity {
    private static final String TAB_TAG_FEEL_TODAY = "feel_today";
    private static final String TAB_TAG_FUTURE_PLANS = "future_plans";

    private TabLayout tabLayout;
    private RecyclerView rvStories;
    private FloatingActionButton fabShare;

    private StoriesAdapter adapter;
    private List<StoryModel> stories = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_stories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.activity_my_stories);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tabLayout = findViewById(R.id.tabLayout);
        setupTabs();

        fabShare = findViewById(R.id.fabShare);

        rvStories = findViewById(R.id.rvStories);
        adapter = new StoriesAdapter(this, stories, this::startStoryView);
        rvStories.setAdapter(adapter);
        rvStories.setLayoutManager(new LinearLayoutManager(this));
        rvStories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fabShare.hide();
                } else {
                    fabShare.show();
                }
            }
        });
        loadStories(StoryModel.Type.FEEL_TODAY);
    }

    private void startStoryView(int position) {
        StoryModel story = stories.get(position);

        StoryDetailsDialog dialog = StoryDetailsDialog.getInstance(story);

        dialog.show(getSupportFragmentManager(), "story");
    }

    private void setupTabs() {
        TabLayout.Tab tabFeelToday = tabLayout.newTab();
        tabFeelToday.setText("Feel Today");
        tabLayout.addTab(tabFeelToday);
        tabFeelToday.setTag(TAB_TAG_FEEL_TODAY);
        tabFeelToday.select();

        TabLayout.Tab tabFuturePlans = tabLayout.newTab();
        tabFuturePlans.setText("Future Plans");
        tabFuturePlans.setTag(TAB_TAG_FUTURE_PLANS);
        tabLayout.addTab(tabFuturePlans);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    if (tab.getTag().toString().equals(TAB_TAG_FEEL_TODAY)) {
                        loadStories(StoryModel.Type.FEEL_TODAY);
                    } else if (tab.getTag().toString().equals(TAB_TAG_FUTURE_PLANS)) {
                        loadStories(StoryModel.Type.FUTURE_PLAN);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void loadStories(StoryModel.Type type) {
        ToFreedomApp.getAppDatabase().storyDao().getAllByType(type.getIndex())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateListView, th -> {
                }); //TODO
    }

    private void updateListView(List<StoryModel> models) {
        stories.clear();
        stories.addAll(models);
        adapter.notifyDataSetChanged();
    }

    private static class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {
        private Context ctx;
        private List<StoryModel> models;
        private ItemClickListener listener;

        public StoriesAdapter(Context ctx, List<StoryModel> models, ItemClickListener l) {
            this.ctx = ctx;
            this.models = models;
            listener = l;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_story, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder vh, int position) {
            StoryModel model = models.get(position);

            if (model.getType() == StoryModel.Type.FEEL_TODAY.getIndex()) {
                vh.imgMood.setVisibility(View.VISIBLE);
                vh.imgMood.setImageResource(ResHelper.moodResources[model.getMoodOption()]);
            } else {
                vh.imgMood.setVisibility(View.GONE);
            }
            vh.txtStoryText.setText(model.getText());
            vh.txtDateCreated.setText(DateFormatHelper.formatForStart(model.getDateCreated(), DateFormat.is24HourFormat(ctx)));

            vh.root.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            View root;
            ImageView imgMood;
            TextView txtStoryText;
            TextView txtDateCreated;

            ViewHolder(View v) {
                super(v);

                root = v.findViewById(R.id.itemRoot);
                imgMood = v.findViewById(R.id.imgMood);
                txtStoryText = v.findViewById(R.id.txtStoryText);
                txtDateCreated = v.findViewById(R.id.txtDateCreated);
            }
        }

        public interface ItemClickListener {
            void onItemClick(int position);
        }
    }
}
