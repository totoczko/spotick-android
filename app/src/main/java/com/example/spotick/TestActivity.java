//package com.example.spotick;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.MenuItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestActivity extends AppCompatActivity {
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    return true;
//                case R.id.navigation_dashboard:
//                    return true;
//                case R.id.navigation_notifications:
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        RecyclerView recyclerView = findViewById(R.id.post_list);
//        List mlist = new ArrayList();
//
//        mlist.add(new cardItem("Lorem iosum", R.drawable.boo, 25));
//        mlist.add(new cardItem("Lorem iosum", R.drawable.boo, 25));
//        mlist.add(new cardItem("Lorem iosum", R.drawable.boo, 25));
//        Adapter adapter = new Adapter(this,mlist);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }
//
//}
