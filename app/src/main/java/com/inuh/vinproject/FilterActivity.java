package com.inuh.vinproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.inuh.vinproject.util.PrefManager;

/**
 * Created by artimus on 22.05.16.
 */
public class FilterActivity extends AppCompatActivity {

    private RadioButton filterStatusAll;
    private RadioButton filterStatusEnd;
    private RadioButton filterStatusInProgres;

    private RadioButton filterSortName;
    private RadioButton filterSortReting;

    private RadioButton checkedStatusButton;
    private RadioButton checkedSortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        filterSortName = (RadioButton) findViewById(R.id.filter_sort_name);
        filterSortName.setOnClickListener(new SortFilterClickListener("name"));
        filterSortReting = (RadioButton) findViewById(R.id.filter_sort_rating);
        filterSortReting.setOnClickListener(new SortFilterClickListener("rating"));

        filterStatusAll = (RadioButton) findViewById(R.id.filter_status_all);
        filterStatusAll.setOnClickListener(new StatusFilterClickListener("all"));
        filterStatusEnd = (RadioButton) findViewById(R.id.filter_status_finish);
        filterStatusEnd.setOnClickListener(new StatusFilterClickListener("finished"));
        filterStatusInProgres = (RadioButton) findViewById(R.id.filter_status_inprogress);
        filterStatusInProgres.setOnClickListener(new StatusFilterClickListener("in_progres"));

        switch (PrefManager.getInstance(this).getSortFilter()){
            case "name":
                filterSortName.setChecked(true);
                checkedSortButton = filterSortName;
                break;
            case "rating":
                filterSortReting.setChecked(true);
                checkedSortButton = filterSortReting;
                break;
            default:
                filterSortName.setChecked(true);
                checkedSortButton = filterSortName;
                break;
        }

        switch (PrefManager.getInstance(this).getStatusFilter()){
            case "all":
                filterStatusAll.setChecked(true);
                checkedStatusButton = filterStatusAll;
                break;
            case "finished":
                filterStatusEnd.setChecked(true);
                checkedStatusButton = filterStatusEnd;
                break;
            case "in_progres":
                filterStatusInProgres.setChecked(true);
                checkedStatusButton = filterStatusInProgres;
                break;
            default:
                filterStatusAll.setChecked(true);
                checkedStatusButton = filterStatusAll;
                break;
        }
    }

    private class SortFilterClickListener implements View.OnClickListener{
        private String sortClause;

        public SortFilterClickListener(String sortClause){
            this.sortClause = sortClause;
        }

        @Override
        public void onClick(View v) {
            if(!checkedSortButton.equals(v)){
                checkedSortButton.setChecked(false);
                checkedSortButton = (RadioButton)v;
                PrefManager.getInstance(FilterActivity.this).setSortFilter(sortClause);

            }
        }
    }

    private class StatusFilterClickListener implements View.OnClickListener{

        private String statusClause;

        public StatusFilterClickListener(String statusClause){
            this.statusClause = statusClause;
        }

        @Override
        public void onClick(View v) {
            if(!checkedStatusButton.equals(v)){
                checkedStatusButton.setChecked(false);
                checkedStatusButton = (RadioButton)v;
                PrefManager.getInstance(FilterActivity.this).setStatusFilter(statusClause);
            }
        }
    }
}
