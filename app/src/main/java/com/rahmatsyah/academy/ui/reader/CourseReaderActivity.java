package com.rahmatsyah.academy.ui.reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import com.rahmatsyah.academy.R;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.ui.reader.content.ModuleContentFragment;
import com.rahmatsyah.academy.ui.reader.list.ModuleListFragment;
import com.rahmatsyah.academy.viewmodel.ViewModelFactory;
import com.rahmatsyah.academy.vo.Resource;

import java.util.List;

public class CourseReaderActivity extends AppCompatActivity implements CourseReaderCallback{

    public static final String EXTRA_COURSE_ID = "extra_course_id";
    private CourseReaderViewModel viewModel;
    private boolean isLarge = false;

    @NonNull
    private static CourseReaderViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(CourseReaderViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couser_reader);

        if (findViewById(R.id.frame_list)!=null){
            isLarge = true;
        }

        viewModel = obtainViewModel(this);

        viewModel.modules.observe(this, initObserver);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String courseId = bundle.getString(EXTRA_COURSE_ID);
            if (courseId != null) {
                viewModel.setCourseId(courseId);
                populateFragment();
            }
        }
    }

    @Override
    public void moveTo(int position, String moduleId) {
        if (!isLarge) {
            Fragment fragment = ModuleContentFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_container, fragment, ModuleContentFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    void removeObserver() {
        viewModel.modules.removeObserver(initObserver);
    }

    private void populateFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!isLarge) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ModuleListFragment.TAG);
            if (fragment == null) {
                fragment = ModuleListFragment.newInstance();
                fragmentTransaction.add(R.id.frame_container, fragment, ModuleListFragment.TAG);
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        } else {
            Fragment fragmentList = getSupportFragmentManager().findFragmentByTag(ModuleListFragment.TAG);
            if (fragmentList == null) {
                fragmentList = ModuleListFragment.newInstance();
                fragmentTransaction.add(R.id.frame_list, fragmentList, ModuleListFragment.TAG);
            }
            Fragment fragmentContent = getSupportFragmentManager().findFragmentByTag(ModuleContentFragment.TAG);
            if (fragmentContent == null) {
                fragmentContent = ModuleContentFragment.newInstance();
                fragmentTransaction.add(R.id.frame_content, fragmentContent, ModuleContentFragment.TAG);
            }
            fragmentTransaction.commit();
        }

    }

    private String getLastReadFromModule(List<ModuleEntity> moduleEntities){
        String lastReadModule = null;

        for (ModuleEntity moduleEntity:moduleEntities){
            if (moduleEntity.isRead()){
                lastReadModule = moduleEntity.getModuleId();
                continue;
            }
            break;
        }

        return lastReadModule;
    }

    private final Observer<Resource<List<ModuleEntity>>> initObserver = modules ->{
        if (modules!=null){
            switch (modules.status){
                case LOADING:
                    break;
                case SUCCESS:
                    if (modules.data!=null && modules.data.size()>0){
                        ModuleEntity firstModule = modules.data.get(0);
                        boolean isFirstModuleRead = firstModule.isRead();

                        if (!isFirstModuleRead){
                            String firstModuleId = firstModule.getModuleId();
                            viewModel.setSelectedModule(firstModuleId);
                        }else {
                            String lastReadModuleId = getLastReadFromModule(modules.data);
                            if (lastReadModuleId!=null){
                                viewModel.setSelectedModule(lastReadModuleId);
                            }
                        }
                    }
                    removeObserver();
                    break;
                case ERROR:
                    Toast.makeText(this,"Gagal Menampilkan Data",Toast.LENGTH_SHORT).show();
                    removeObserver();
                    default:
                        break;
            }
        }
    };
}
