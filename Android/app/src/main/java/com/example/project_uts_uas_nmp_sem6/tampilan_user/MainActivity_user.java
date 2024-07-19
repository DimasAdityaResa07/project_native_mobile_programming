package com.example.project_uts_uas_nmp_sem6.tampilan_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_uts_uas_nmp_sem6.R;

public class MainActivity_user extends AppCompatActivity {
FrameLayout frameDaftarUser;
String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_user);
        frameDaftarUser=findViewById(R.id.frame_datauser);

        //Ambil data dari Intent sebelumnya // Dimas
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        frameDaftarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity_user.this,email,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity_user.this, edit_profile_user.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }
}