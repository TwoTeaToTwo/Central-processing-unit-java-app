package com.twoteatotwo.lesson02;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twoteatotwo.lesson02.adapter.MainAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Compare_Content_Activity extends AppCompatActivity {
    private ArrayList<TextView> text_content = new ArrayList<>();
    private ArrayList<ProgressBar> bar_content = new ArrayList<>();

    private ImageButton iv_cpu_image;

    private int category;
    private int position;

    private float value_i;
    private float value_max = 2500;
    private String value_s;
    private float count_i;
    private float count_max = 32;
    private String count_s;
    private float cache_i;
    private float cache_max = 32;
    private String cache_s;
    private String name;
    private float progress;

    private boolean isWiki;

    private Dialog image_dialog;
    private Dialog text_dialog;

    private ArrayList<Integer> array_image_elbrus = new ArrayList<>();
    private ArrayList<Integer> array_image_baikal = new ArrayList<>();

    private ArrayList<String> arrayList1 = new ArrayList<>();

    private RecyclerView mainRecycleView;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.compare_content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close);
        actionBar.setBackgroundDrawable(getDrawable(R.color.red_500));
        actionBar.setTitle("Сравнение");

        text_content.add(findViewById(R.id.cpu_name1));
        text_content.add(findViewById(R.id.cpunitcount1));
        text_content.add(findViewById(R.id.cpuvalue1));
        text_content.add(findViewById(R.id.cpucache1));

        iv_cpu_image = findViewById(R.id.cpuimage1);

        bar_content.add(findViewById(R.id.cpunitprogressbar1));
        bar_content.add(findViewById(R.id.cpuvalueprogressbar1));
        bar_content.add(findViewById(R.id.cpucahceprogressbar1));

        text_content.add(findViewById(R.id.cpu_name2));
        text_content.add(findViewById(R.id.cpunitcount2));
        text_content.add(findViewById(R.id.cpuvalue2));
        text_content.add(findViewById(R.id.cpucache2));


        bar_content.add(findViewById(R.id.cpunitprogressbar2));
        bar_content.add(findViewById(R.id.cpuvalueprogressbar2));
        bar_content.add(findViewById(R.id.cpucahceprogressbar2));

        reciveIntent();

    }
    private void reciveIntent() {
        Intent i = getIntent();
        if (i != null) {

            category = i.getIntExtra("category1", 0);
            //Log.d("Mylog","1: " + category);
            position = i.getIntExtra("position1", 0);
            name = i.getStringExtra("name1");
            isWiki = i.getBooleanExtra("isWiki1", false);
            arrayList1.addAll(i.getStringArrayListExtra("list1"));

            setDataFromArray();
            text_content.get(0).setText(name);
            text_content.get(1).setText(count_s);
            text_content.get(2).setText(value_s);
            text_content.get(3).setText(cache_s);

            progress = Math.min((Math.round((count_i / count_max) * 100)), 100);
            bar_content.get(0).setProgress(Math.round(progress));
            progress = Math.min(Math.round((value_i / value_max) * 100), 100);
            bar_content.get(1).setProgress(Math.round(progress));
            progress = Math.min(Math.round((cache_i / cache_max) * 100), 100);
            bar_content.get(2).setProgress(Math.round(progress));

            if (isWiki) {
                array_image_elbrus.add(R.drawable.im_elbrus2000);
                array_image_elbrus.add(R.drawable.im_elb_s);
                array_image_elbrus.add(R.drawable.im_elb_2cp);
                array_image_elbrus.add(R.drawable.im_elb_4c);
                array_image_elbrus.add(R.drawable.im_elb_1cp);
                array_image_elbrus.add(R.drawable.im_elb_8c);
                array_image_elbrus.add(R.drawable.im_elb_8cb);
                array_image_elbrus.add(R.drawable.im_elb_2c3);
                array_image_elbrus.add(R.drawable.im_elb_12c);
                array_image_elbrus.add(R.drawable.im_elb_16c);
            } else {
                array_image_elbrus.add(R.drawable.im_elb_2cp);
                array_image_elbrus.add(R.drawable.im_elb_8c);
                array_image_elbrus.add(R.drawable.im_elbrus2000);
                array_image_elbrus.add(R.drawable.im_elb_1cp);
                array_image_elbrus.add(R.drawable.im_elb_2c3);
                array_image_elbrus.add(R.drawable.im_elb_4c);
                array_image_elbrus.add(R.drawable.im_elb_8cb);
                array_image_elbrus.add(R.drawable.im_elb_12c);
                array_image_elbrus.add(R.drawable.im_elb_16c);
                array_image_elbrus.add(R.drawable.im_elb_r500);
                array_image_elbrus.add(R.drawable.im_elb_r500s);
                array_image_elbrus.add(R.drawable.im_elb_r1000);
                array_image_elbrus.add(R.drawable.im_elb_r2000);
                array_image_elbrus.add(R.drawable.im_elb_r2000p);
                array_image_elbrus.add(R.drawable.im_elb_s);
            }

            array_image_baikal.add(R.drawable.im_baikal_t);
            array_image_baikal.add(R.drawable.im_baikal_m);
            array_image_baikal.add(R.drawable.im_baikal_s);

            if (category == 0) {

                if (position < array_image_elbrus.size()) {
                    iv_cpu_image.setImageResource(array_image_elbrus.get(position));
                } else {

                }

            } else if (category == 1) {

                if (position < array_image_baikal.size()) {
                    iv_cpu_image.setImageResource(array_image_baikal.get(position));
                } else {

                }

            }

            mainRecycleView = findViewById(R.id.recyclerViewCompare1);

            mainAdapter = new MainAdapter(this, getDrawable(R.color.red_500));
            mainRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mainRecycleView.setAdapter(mainAdapter);
            mainAdapter.updateAdapter(arrayList1);
            mainRecycleView.setNestedScrollingEnabled(false);

            //next cpu----------------------------------------------------------------


            category = i.getIntExtra("category2", 0);
            Log.d("Mylog","2: " + category);
            position = i.getIntExtra("position2", 0);
            name = i.getStringExtra("name2");
            isWiki = i.getBooleanExtra("isWiki2", false);

            arrayList1.clear();
            arrayList1.addAll(i.getStringArrayListExtra("list2"));

            setDataFromArray();
            text_content.get(4).setText(name);
            text_content.get(5).setText(count_s);
            text_content.get(6).setText(value_s);
            text_content.get(7).setText(cache_s);

            iv_cpu_image = findViewById(R.id.cpuimage2);

            progress = Math.min((Math.round((count_i / count_max) * 100)), 100);
            bar_content.get(3).setProgress(Math.round(progress));
            progress = Math.min(Math.round((value_i / value_max) * 100), 100);
            bar_content.get(4).setProgress(Math.round(progress));
            progress = Math.min(Math.round((cache_i / cache_max) * 100), 100);
            bar_content.get(5).setProgress(Math.round(progress));

            array_image_elbrus.clear();
            array_image_baikal.clear();

            if (isWiki) {
                array_image_elbrus.add(R.drawable.im_elbrus2000);
                array_image_elbrus.add(R.drawable.im_elb_s);
                array_image_elbrus.add(R.drawable.im_elb_2cp);
                array_image_elbrus.add(R.drawable.im_elb_4c);
                array_image_elbrus.add(R.drawable.im_elb_1cp);
                array_image_elbrus.add(R.drawable.im_elb_8c);
                array_image_elbrus.add(R.drawable.im_elb_8cb);
                array_image_elbrus.add(R.drawable.im_elb_2c3);
                array_image_elbrus.add(R.drawable.im_elb_12c);
                array_image_elbrus.add(R.drawable.im_elb_16c);
            } else {
                array_image_elbrus.add(R.drawable.im_elb_2cp);
                array_image_elbrus.add(R.drawable.im_elb_8c);
                array_image_elbrus.add(R.drawable.im_elbrus2000);
                array_image_elbrus.add(R.drawable.im_elb_1cp);
                array_image_elbrus.add(R.drawable.im_elb_2c3);
                array_image_elbrus.add(R.drawable.im_elb_4c);
                array_image_elbrus.add(R.drawable.im_elb_8cb);
                array_image_elbrus.add(R.drawable.im_elb_12c);
                array_image_elbrus.add(R.drawable.im_elb_16c);
                array_image_elbrus.add(R.drawable.im_elb_r500);
                array_image_elbrus.add(R.drawable.im_elb_r500s);
                array_image_elbrus.add(R.drawable.im_elb_r1000);
                array_image_elbrus.add(R.drawable.im_elb_r2000);
                array_image_elbrus.add(R.drawable.im_elb_r2000p);
                array_image_elbrus.add(R.drawable.im_elb_s);
            }

            array_image_baikal.add(R.drawable.im_baikal_t);
            array_image_baikal.add(R.drawable.im_baikal_m);
            array_image_baikal.add(R.drawable.im_baikal_s);

            if (category == 0) {

                if (position < array_image_elbrus.size()) {
                    iv_cpu_image.setImageResource(array_image_elbrus.get(position));
                } else {

                }

            } else if (category == 1) {

                if (position < array_image_baikal.size()) {
                    iv_cpu_image.setImageResource(array_image_baikal.get(position));
                } else {

                }

            }
            mainRecycleView = findViewById(R.id.recyclerViewCompare2);

            mainAdapter = new MainAdapter(this, getDrawable(R.color.red_500));
            mainRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mainRecycleView.setAdapter(mainAdapter);
            mainAdapter.updateAdapter(arrayList1);
            mainRecycleView.setNestedScrollingEnabled(false);
        }
    }

    public void makeToast(String string) {
        Toast toast = Toast.makeText(getApplicationContext(),
                string, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setDataFromArray(){
        for(int i = 0; i <arrayList1.size();i++){
            if(arrayList1.get(i).contains("МГц") || arrayList1.get(i).contains("ГГц")){
                String str = "";
                String tmp = arrayList1.get(i);
                for(int j = 0; j < arrayList1.get(i).length(); j++){
                    if( str.contains("до")){
                        str = "";
                    }

                    if( (str.contains("МГц") || str.contains("ГГц")) && !(str.contains("DDR"))){
                        break;
                    }
                    str += tmp.charAt(j);
                }

                value_s = str.replaceAll("[^0-9]", "");
                value_i = Float.parseFloat(value_s);
                if(value_i < 60) {
                    value_i*=100;
                    value_s+="00";
                }
                value_s = "Частота: " + value_s + " МГц";
                break;
            }
        }


        for(int i = 0; i <arrayList1.size();i++){
            if(arrayList1.get(i).contains("ядер")){
                String str = "";
                String tmp = arrayList1.get(i);
                for(int j = 0; j < arrayList1.get(i).length(); j++){
                    if( str.contains("ядер")){
                        str = "";
                    }
                    if( (tmp.charAt(j) == 'я' || tmp.charAt(j) == ' ') && (str.codePoints().filter(Character::isDigit).count() > 0)){
                        break;
                    }
                    str += tmp.charAt(j);

                }

                count_s = str.replaceAll("[^0-9]", "");
                count_i = Integer.parseInt(count_s);
                count_s = "Кол-во ядер: " + count_s;
            }
        }

        cache_i = 1;
        cache_s = "Кэш: 0 МБ";
        for(int i = 0; i <arrayList1.size();i++){
            if(arrayList1.get(i).contains("МБ") || arrayList1.get(i).contains("Мб")){
                String str = "";
                String tmp = arrayList1.get(i);
                for(int j = 0; j < arrayList1.get(i).length() - 1; j++){
                    // if( tmp.charAt(j) == 'М'){
                    //     break;
                    // }
                    if( (str.contains("МБ") || str.contains("Мб")) && !(tmp.contains("L3")) && !(tmp.contains("в каждом"))){
                        break;
                    }
                    if( str.contains("L1") || str.contains("L2") || str.contains("L3") || str.contains("Кбайт") || str.contains("2-го") || str.contains("в каждом") || str.contains("3-го") || str.contains("4-го")){
                        str = "";
                    }
                    if((tmp.charAt(j+1) != 'К') && (tmp.charAt(j) == ' ') && (str.codePoints().filter(Character::isDigit).count() > 0) && !(tmp.contains("L3")) && !(tmp.contains("в каждом")) ){
                        break;
                    }
                    str += tmp.charAt(j);
                }

                cache_s = str.replaceAll("[^0-9]", "");
                if(cache_s.length() == 0){ cache_s ="0";}
                cache_i = Integer.parseInt(cache_s);
                cache_s = "Кэш: " + cache_s + " МБ";
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //action bar close button
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
