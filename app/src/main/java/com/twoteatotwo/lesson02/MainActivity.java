package com.twoteatotwo.lesson02;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twoteatotwo.lesson02.DataBase.DBHelper;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {





    private DrawerLayout drawer;
    private ListView list;
    private String[] array;
    private ArrayAdapter<String> adapter;
    private NavigationView navigationView;
    private androidx.appcompat.widget.Toolbar toolbar; //using new toolbar
    private int index_category;
    private DBHelper dbHelper;
    private Dialog dialog;
    private Document document;
    private ArrayList<String> elbrus_name_array = new ArrayList<>();
    private ArrayList<String> baikal_name_array = new ArrayList<>();
    private String[] elbrusLinks;
    private String[] baikalLinks;
    private ArrayList<String> elbrus_description_array = new ArrayList<>();
    private ArrayList<String> baikal_description_array = new ArrayList<>();
    private ArrayList<String> arrayList_of_elbrus_descriptions = new ArrayList<>();
    private ArrayList<String> arrayList_of_baikal_descriptions = new ArrayList<>();
    private Thread thread;
    private int tap_count = 0;
    public static final int APP_VERSION = 3;
    private int new_appVersion = 0;
    private String updateLink;
    final Handler handler = new Handler();

    private int category1 = 0;
    private int position1 = 0;
    private String name1 = "";
    private boolean isWiki1 = false;
    private ArrayList<String> array_description1 = new ArrayList<>();
    private boolean isError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        elbrusLinks = getResources().getStringArray(R.array.elbrus_link);
        baikalLinks = getResources().getStringArray(R.array.baikal_link);

        dbHelper = new DBHelper(this);

        getDataFromDB();

        if(elbrus_name_array.size() == 0) {
            makeDialog(4,"");
        }

        list = findViewById(R.id.list_view);
        array = elbrus_name_array.toArray( new String[0]);     //getResources().getStringArray(null) ;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array) ) );
        list.setAdapter(adapter);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getNewVersion();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (index_category != 3) {
                    getArrayDataFromDB();
                    getDataFromArrayById(index_category, i);
                    category1 = 0;
                    position1 = 0;
                    name1 = "";
                    isWiki1 = false;
                    array_description1.clear();

                    Intent intent = new Intent(MainActivity.this, Text_Content_Activity.class);
                    intent.putExtra("category", index_category);
                    intent.putExtra("position", i);

                    if (index_category == 0) {
                        intent.putExtra("name", elbrus_name_array.get(i));
                        intent.putExtra("isWiki", !(elbrus_name_array.get(0).contains("Эльбрус-2С+")));
                        intent.putStringArrayListExtra("list", elbrus_description_array);

                    } else {
                        if (index_category == 1) {

                            intent.putExtra("name", baikal_name_array.get(i));
                            intent.putStringArrayListExtra("list", baikal_description_array);
                        }
                    }

                    startActivity(intent);
                    tap_count = 0;
                } else {
                    getArrayDataFromDB();
                    tap_count+=1;
                    if(tap_count == 1){
                        int category = 0;
                        //Log.d("Mylog","1: " + i);
                        if(i > 14){category = 1;}

                        if (category == 0) {
                            category1 = 0;
                            position1 = i;
                            getDataFromArrayById(category, position1);
                            name1 = elbrus_name_array.get(position1);
                            isWiki1 = !(elbrus_name_array.get(0).contains("Эльбрус-2С+"));
                            array_description1.addAll(elbrus_description_array);

                        } else {
                            if (category == 1) {
                                category1 = 1;
                                position1 = i - 15;
                                getDataFromArrayById(category, position1);
                                name1 = baikal_name_array.get(position1);
                                isWiki1 = !(baikal_name_array.get(0).contains("Эльбрус-2С+"));
                                array_description1.addAll(baikal_description_array);
                            }
                        }
                    }
                    if(tap_count > 1 && i != position1){
                        Intent intent = new Intent(MainActivity.this, Compare_Content_Activity.class);
                            //Log.d("Mylog","1: " + category1);
                        intent.putExtra("category1", category1);
                            //Log.d("Mylog","1: " + position1);
                        intent.putExtra("position1", position1);
                           // Log.d("Mylog","1: " + name1);
                        intent.putExtra("name1", name1);
                           // Log.d("Mylog","1: " + false);
                        intent.putExtra("isWiki1", isWiki1);
                           // Log.d("Mylog","1: " + array_description1.toString());
                        intent.putStringArrayListExtra("list1", array_description1);

                        ArrayList<String> allArray_t = new ArrayList<>();
                        allArray_t.addAll(elbrus_name_array);
                        allArray_t.addAll(baikal_name_array);
                        int category2 = 0;
                        String name2 = allArray_t.get(i);
                        ArrayList<String> array_description2 = new ArrayList<>();

                        if(i > 14){
                            i-=15;
                            category2 = 1;
                            getDataFromArrayById(category2, i);
                            array_description2.addAll(baikal_description_array);
                        } else {
                            getDataFromArrayById(category2, i);
                            array_description2.addAll(elbrus_description_array);
                        }

                        intent.putExtra("category2", category2);
                        //.d("Mylog", "2: " + category2);
                        intent.putExtra("position2", i);
                        //Log.d("Mylog", "2: " +i);
                        intent.putExtra("name2", name2);
                        intent.putExtra("isWiki2", false);
                        intent.putStringArrayListExtra("list2", array_description2);

                        startActivity(intent);
                        array_description1.clear();
                        tap_count = 0;
                    }

                }
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        toolbar.setTitle(R.string.menu_elbrus);
        index_category = 0;
        return true;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.id_elbrus){
            array = elbrus_name_array.toArray(new String[0]);
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();

            toolbar.setTitle(R.string.menu_elbrus);
            index_category = 0;
            tap_count = 0;
        } else if(id == R.id.id_baikal){
            array = baikal_name_array.toArray(new String[0]);
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();

            toolbar.setTitle(R.string.menu_baikal);
            index_category = 1;
            tap_count = 0;
        } else if(id == R.id.id_db_updater){
            makeDialog(0,"");
            tap_count = 0;
        } else if(id == R.id.id_compare){
            ArrayList<String> allArray_t = new ArrayList<>();
            allArray_t.addAll(elbrus_name_array);
            allArray_t.addAll(baikal_name_array);
            array = allArray_t.toArray(new String[0]);
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();
            toolbar.setTitle(R.string.menu_compare);
            index_category = 3;
        } else if(id == R.id.id_shop){
            Intent intent = new Intent(MainActivity.this, Web_Content_Activity.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    public void onClickAbout(MenuItem item) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.content_about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void onClickupdaterButtonYes(View view) throws IOException {
        dialog.cancel();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        makeDialog(1,"");
                    }
                });
                isError = false;
                getElbrusName();

                if(!isError) {
                    while (elbrus_name_array.size() == 0) {
                    }
                    addNewDataToDB(true, DBHelper.TABLE_NAMES_ELBRUS, elbrus_name_array);
                    addNewDataToDB(false, DBHelper.TABLE_NAMES_ELBRUS, elbrus_name_array);
                    addNewDataToDB(true, DBHelper.TABLE_NAMES_BAIKAL, baikal_name_array);
                    addNewDataToDB(false, DBHelper.TABLE_NAMES_BAIKAL, baikal_name_array);

                    getCPUElbrusData();
                    getCPUBaikalData();
                }
                if(!isError) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                            makeDialog(5, "Данные успешно обновлены!");

                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                            makeDialog(2, "Данные не обновлены!");

                        }
                    });
                }
            }
        };
        thread = new Thread(runnable);
        thread.start();

    }

    private void addNewDataToDB(boolean clear, String table, ArrayList<String>  arrayList){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if (clear) {
            database.delete(table, null, null);
            //Log.d("Mylog","delete: " + table);
            return;
        }
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        String inputString= gson.toJson(arrayList);
        contentValues.put(DBHelper.KEY_NAME, inputString);
        database.insert(table, null, contentValues);

    }


    private void getDataFromDB(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String outputString;
        Gson gson = new Gson();

        Cursor cursor = database.query(DBHelper.TABLE_NAMES_ELBRUS, null, null, null,null,null,null);
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                outputString = cursor.getString(nameIndex);
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                elbrus_name_array = gson.fromJson(outputString, type);
            }
        }
        cursor.close();

        cursor = database.query(DBHelper.TABLE_NAMES_BAIKAL, null, null, null,null,null,null);
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                outputString = cursor.getString(nameIndex);
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                baikal_name_array = gson.fromJson(outputString, type);
            }
        }
        cursor.close();


    }

    private void getDataFromArrayById( int id, int position){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String outputString;
        switch (id){
            case 0:
                elbrus_description_array.clear();
                outputString = arrayList_of_elbrus_descriptions.get(position);
                elbrus_description_array = gson.fromJson(outputString, type);
                break;
            case 1:
                baikal_description_array.clear();
                outputString = arrayList_of_baikal_descriptions.get(position);
                baikal_description_array = gson.fromJson(outputString, type);
                break;
        }
    }

    private void getArrayDataFromDB() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String outputString;
        Gson gson = new Gson();
        arrayList_of_elbrus_descriptions.clear();
        Cursor cursor = database.query(DBHelper.TABLE_ELBRUS, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                outputString = cursor.getString(nameIndex);
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                arrayList_of_elbrus_descriptions = gson.fromJson(outputString, type);
            }
            cursor.close();

        }

        arrayList_of_baikal_descriptions.clear();
        cursor = database.query(DBHelper.TABLE_BAIKAL, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                outputString = cursor.getString(nameIndex);
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                arrayList_of_baikal_descriptions = gson.fromJson(outputString, type);
            }
            cursor.close();

        }

    }

    public void onClickupdaterButtonNo(View view) {
        dialog.cancel();
    }

    public void makeDialog(int type, String text){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (type == 0){
            dialog.setContentView(R.layout.db_updater_dialog);
        } else if(type == 1){
            dialog.setContentView(R.layout.loading_dialog);
            dialog.setCancelable(false);
        } else if(type == 2){
            dialog.setContentView(R.layout.error_dialog);
        } else if(type == 3){
            dialog.setContentView(R.layout.update_dialog);
        } else if(type == 4){
            dialog.setContentView(R.layout.image_dialog);
            ImageView image = dialog.findViewById(R.id.imageViewDialog);
            image.setImageResource(R.drawable.ic_tutorial);
            image.setMaxHeight(2000);
            image.setMaxWidth(2000);
        } else if(type == 5){
            dialog.setContentView(R.layout.text_dialog);
            TextView textview = dialog.findViewById(R.id.dialog_text);
            textview.setText(text);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void makeToast(String string){
        Toast toast = Toast.makeText(getApplicationContext(),
                string, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getElbrusName(){
                try {
                    document = Jsoup.connect("http://www.mcst.ru/chips").get();
                    Elements elements = document.getElementsByTag("ul");
                    Element element = elements.get(17);
                    Elements elements_from_element = element.children();
                    elbrus_name_array.clear();
                    for (int i = 0; i < elements_from_element.size(); i++) {
                        String temp_s = elements_from_element.get(i).text().toString();
                        String temp_res = "";
                        for(int t = 0; t < temp_s.length(); t++){
                            if(temp_s.charAt(t) != '('){
                                temp_res += temp_s.charAt(t);
                            } else {
                                break;
                            }
                        }
                        elbrus_name_array.add(temp_res);
                    }

                    baikal_name_array.clear();
                    baikal_name_array.add("Baikal-T1");
                    baikal_name_array.add("Baikal-M");
                    baikal_name_array.add("Baikal-S");
                } catch (IOException e) {

                    isError = true;





                    //maybe in new update-----------------------------------------------------------------------------------------------------
                    /*
                    e.printStackTrace();
                    Log.d("Mylog", "connection error");
                    try {
                    document = Jsoup.connect("https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81_(%D0%BF%D1%80%D0%BE%D1%86%D0%B5%D1%81%D1%81%D0%BE%D1%80%D0%BD%D0%B0%D1%8F_%D0%B0%D1%80%D1%85%D0%B8%D1%82%D0%B5%D0%BA%D1%82%D1%83%D1%80%D0%B0)").get();
                    Elements elements = document.getElementsByTag("table");
                    Element element = elements.get(0);
                    Elements elements_from_element = element.getElementsByTag("tr");
                    Element tr_element = elements_from_element.get(0);
                    Elements td_elements = tr_element.children();
                    elbrus_name_array.clear();
                    for (int i = 1; i < td_elements.size(); i++) {
                        String temp_s = td_elements.get(i).text().toString();
                        String temp_res = "";
                        for(int t = 0; t < temp_s.length(); t++){
                            if(temp_s.charAt(t) != '['){
                                temp_res += temp_s.charAt(t);
                            } else {
                                break;
                            }
                        }
                        elbrus_name_array.add(temp_res);
                    }
                    } catch (IOException ioException) {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                makeToast("Ошибка подключение к резервному источнику");
                            }
                        });

                        ioException.printStackTrace();
                    }
                    */
                }
            }


    private void getCPUElbrusData(){

                try {
                    arrayList_of_elbrus_descriptions.clear();
                        for (int j = 0; j < elbrusLinks.length; j++) {
                            if(isError){return;}
                            document = Jsoup.connect(elbrusLinks[j]).get();
                            Elements elements = document.getElementsByTag("tbody");
                            Element element;
                            switch (j) {
                                case 1:
                                    element = elements.get(1);
                                    break;
                                case 4:
                                    element = elements.get(1);
                                    break;
                                case 6:
                                    element = elements.get(1);
                                    break;
                                case 7:
                                    element = elements.get(1);
                                    break;
                                case 8:
                                    element = elements.get(1);
                                    break;
                                case 12:
                                    element = elements.get(1);
                                    break;
                                case 13:
                                    element = elements.get(1);
                                    break;
                                default:
                                    element = elements.get(0);
                                    break;
                            }
                            Elements elements_from_element = element.children();
                            elbrus_description_array.clear();
                            for (int i = 0; i < elements_from_element.size(); i++) {
                                if(elements_from_element.get(i).children().size() == 1 || elements_from_element.get(i).toString().contains("Характеристика")){
                                    elbrus_description_array.add("*");
                                } else elbrus_description_array.add(elements_from_element.get(i).text());
                            }
                            Gson gson = new Gson();
                            String inputString = gson.toJson(elbrus_description_array);
                            arrayList_of_elbrus_descriptions.add(inputString);

                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    addNewDataToDB(true,DBHelper.TABLE_ELBRUS, elbrus_description_array);
                    addNewDataToDB(false, DBHelper.TABLE_ELBRUS, arrayList_of_elbrus_descriptions);

                } catch (IOException e) {
                    e.printStackTrace();
                    isError = true;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ошибка получения данных процессоров");
                        }
                    });
                }

            }

    private void getCPUBaikalData(){

        try {

            arrayList_of_baikal_descriptions.clear();
            for (int j = 0; j < baikalLinks.length; j++) {
                if(isError){return;}
                document = Jsoup.connect(baikalLinks[j]).get();
                Elements elements = document.getElementsByTag("table");
                Element element = elements.get(1);
                Elements tbodys = element.getElementsByTag("tbody");
                Element tbody = tbodys.get(0);
                Elements elements_from_element = tbody.children();
                baikal_description_array.clear();
                baikal_description_array.add("*");

                for (int i = 0; i < elements_from_element.size(); i++) {
                    String str = elements_from_element.get(i).text();
                    String res = "";
                    boolean b = true;
                    for(int z = 0; z < str.length(); z++){
                        if(str.charAt(z) == '['){b = false;}
                        if(b){res+=str.charAt(z);}
                        if(str.charAt(z) == ']'){b=true;}
                    }
                    baikal_description_array.add(res);

                }

                Gson gson = new Gson();
                String inputString = gson.toJson(baikal_description_array);
                arrayList_of_baikal_descriptions.add(inputString);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            addNewDataToDB(true,DBHelper.TABLE_BAIKAL, baikal_description_array);
            addNewDataToDB(false, DBHelper.TABLE_BAIKAL, arrayList_of_baikal_descriptions);

        } catch (IOException e) {
            e.printStackTrace();
            isError = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    makeToast("Ошибка получения данных процессоров");
                }
            });
        }

    }

    private void getNewVersion(){
        Runnable runnable_t = new Runnable() {
            @Override
            public void run() {
                try {
                    document = Jsoup.connect("https://appversion.wilank.ru").get();
                    String tmp;
                    String str = "";
                    Elements elements = document.getElementsByTag("a");
                    tmp = elements.get(0).toString();
                    for(int i = 0; i < tmp.length()-4; i++){
                        if(str.contains("<a>")){ str ="";}
                        str+=tmp.charAt(i);
                    }
                    new_appVersion = Integer.parseInt(str);

                    str ="";
                    tmp = elements.get(1).toString();
                    for(int i = 0; i < tmp.length()-4; i++){
                        if(str.contains("<a>")){ str ="";}
                        str+=tmp.charAt(i);
                    }
                    updateLink = str;

                    if(new_appVersion > APP_VERSION){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                makeDialog(3,"");
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.d("Mylog", "Error:" + e);
                }
            }
        };
        Thread thread_t = new Thread(runnable_t);
        thread_t.start();

    }

    public void onClickUpdaterVersionButtonYes(View view) {
        Uri uri = Uri.parse("http://" + updateLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onClickImageDialog(View view) {
        dialog.cancel();
    }

    public void onClickUpdateButtonStop(View view) {
        isError = true;
        getDataFromDB();

    }

   /* private void getWeb(String url){
        try {
            document = Jsoup.connect(url).get();

            Elements elements = document.getElementsByTag("tbody");
            Element element = elements.get(0);
            Elements elements_from_element = element.children();
            for(int i = 1; i <= elements_from_element.size(); i++) {

            }

        } catch (IOException e) {
            Log.d("Mylog", "Error:" + e);
        }

    }
    */

}