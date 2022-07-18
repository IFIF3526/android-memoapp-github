package com.ifif3526.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ifif3526.memoapp.adapter.MemoAdapter;
import com.ifif3526.memoapp.data.DatabaseHandler;
import com.ifif3526.memoapp.model.Memo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText editSearch;
    ImageView imgSearch;
    ImageView imgDelete;

    RecyclerView recyclerView;
    MemoAdapter adapter;
    ArrayList<Memo> memoList;
    
    // 깃허브 연동 완료

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);

        // 1. 에딧서치와 이미지뷰들을 화면에 연결하기.
        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        imgDelete = findViewById(R.id.imgDelete);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        // 2. 이미지뷰를 버튼으로 동작가능하게 하기.
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 3. 유저가 입력한 텍스트 가져오기
                String keyword = editSearch.getText().toString().trim();

                // 4. 디비에 연결, 쿼리문 만들어 가져오기.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);

                // 5. 데이터베이스 헨들러로가서 메소드 만들기
                // 6. 유저가 입력한 텍스트가 포함된 메모를 디비에서 가져오기
                memoList = db.searchMemo(keyword);

                // 7. 화면에 표시
                adapter = new MemoAdapter(MainActivity.this, memoList);
                recyclerView.setAdapter(adapter);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 에디트 텍스트의 문자열을 지운다.
                editSearch.setText(null);

                // 2. DB 에 저장되어있는 모든 메모를 가져와야 한다.
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.getAllMemo();
                
                // 3. 가져온 메모를 화면에 표시
                adapter = new MemoAdapter(MainActivity.this, memoList);
                recyclerView.setAdapter(adapter);
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 키워드 검색 에디트텍스트에 글자를 쓸때마다,
                // 자동으로 해당 검색어를 가져와서, 디비에서 쿼리해서
                // 검색 결과를 화면에 표시해주는 기능 개발.

                String keyword = editSearch.getText().toString().trim();
                Log.i("MyMemoApp", keyword);

                if (keyword.length() < 2) {
                    return;
                }

                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.searchMemo(keyword);
                adapter = new MemoAdapter(MainActivity.this, memoList);
                recyclerView.setAdapter(adapter);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        // 검색키워드 에디트 텍스트에 글자가 있으면, 없애도록한다.
        editSearch.setText(null);

        // 데이터랑 연결!
        // 메모데이터가 없다!!!! 디비에서 가져오자.
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        memoList = db.getAllMemo();
        adapter = new MemoAdapter(MainActivity.this, memoList);
        recyclerView.setAdapter(adapter);
        db.close();

    }
}






