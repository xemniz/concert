package ru.xmn.concert.gigs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ru.xmn.concert.R;
import ru.xmn.concert.databinding.ActivityMainBinding;
import ru.xmn.concert.mvp.view.ActivityUtils;

public class MainActivity extends AppCompatActivity {
    private GigsContract.Presenter mGigsPresenter;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        GigsFragment gigsFragment =
                (GigsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (gigsFragment == null) {
            // Create the fragment
            gigsFragment = GigsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), gigsFragment, R.id.fragment_container);
        }

        // Create the presenter
        mGigsPresenter = new GigsPresenter(
                GigsModel.getInstance(),
                gigsFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vk_item:
                mGigsPresenter.changeFilter(GigsFragment.FILTER_VK);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
    }
}
