package com.nxinaim.dictionary;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbhelper;
    SearchView searchView;

    ArrayList<HashMap<String, String>> arrayList;
    ArrayList<HashMap<String, String>> originalList;

    HashMap <String, String> hashMap;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.simpleSearchView);
        dbhelper = new DatabaseHelper(this);

        // Get all data from database
        Cursor cursor = dbhelper.getAllData();

        if (cursor!=null && cursor.getCount()>0){

            arrayList = new ArrayList<>();
            originalList = new ArrayList<>();
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String word = cursor.getString(1);
                String meaning = cursor.getString(2);
                String partsOfSpeech = cursor.getString(3);
                String example = cursor.getString(4);

                hashMap = new HashMap<>();
                hashMap.put("word", word);
                hashMap.put("meaning", meaning);
                hashMap.put("partsOfSpeech", partsOfSpeech);
                hashMap.put("example", example);
                arrayList.add(hashMap);
                originalList.add(hashMap);

            }
            adapter = new MyAdapter();
            listView.setAdapter(adapter);
        }

        // SearchView Listener সেটআপ
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);
                    return true;
                }
            });
        }
    }

    //=========================

    // ফিল্টার করার মেথড (HashMap নির্ভর)
    private void filter(String text) {
        if (arrayList == null || originalList == null) return;

        arrayList.clear();

        if (text.isEmpty()) {
            arrayList.addAll(originalList);
        } else {
            String query = text.toLowerCase().trim();
            for (HashMap<String, String> item : originalList) {
                String word = item.get("word");
                if (word != null && word.toLowerCase().contains(query)) {
                    arrayList.add(item);
                }
            }
        }

        if (adapter != null) adapter.notifyDataSetChanged(); // লিস্ট রিফ্রেশ করবে
    }

    //=======================
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.item, parent, false);

            TextView tvWord = view.findViewById(R.id.tvWord);
            TextView tvMeaning = view.findViewById(R.id.tvMeaning);
            TextView tvExample = view.findViewById(R.id.tvExample);

            hashMap = arrayList.get(position);
            String word = hashMap.get("word");
            String meaning = hashMap.get("meaning");
            String partsOfSpeech = hashMap.get("partsOfSpeech");
            String example = hashMap.get("example");

            tvWord.setText(word + " (" + partsOfSpeech + ")");
            tvMeaning.setText(meaning);
            tvExample.setText(example);

            return view;
        }
    }

}//end
