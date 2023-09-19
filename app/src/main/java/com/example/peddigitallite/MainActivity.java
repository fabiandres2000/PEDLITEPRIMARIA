package com.example.peddigitallite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.content.pm.ActivityInfo;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.peddigitallite.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.ped);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_juegos, R.id.nav_ponteAPrueba, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        preferencias(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    public void preferencias(Context context){
        final String[] nombre_ = new String[1];
        final String[] correo_ = new String[1];

        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        if (!loginPreferences.contains("nombre")) { //How can I ask here?
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("DATOS DE USUARIO");
            alertDialog.setMessage("Ingrese los siguientes datos");
            alertDialog.setIcon(R.drawable.data);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText nombre = new EditText(context);
            nombre.setHint("Nombre");
            layout.addView(nombre);


            final EditText correo = new EditText(context);
            correo.setHint("Correo");
            layout.addView(correo); // Another add method

            int paddingDp = 25;
            float density = context.getResources().getDisplayMetrics().density;
            int paddingPixel = (int) (paddingDp * density);
            layout.setPadding(paddingPixel, 0, paddingPixel, 0);


            alertDialog.setView(layout);

            alertDialog.setPositiveButton("Guardar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            nombre_[0] = nombre.getText().toString();
                            correo_[0] = correo.getText().toString();

                            if (nombre_[0] != "" && correo_[0] != "") {
                                SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                                editor.putString("nombre", nombre_[0]);
                                editor.putString("correo", correo_[0]);
                                editor.apply();

                                cambiar_datos();
                            } else {
                                Toast.makeText(MainActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            alertDialog.show();
        }else{
            cambiar_datos();
        }
    }

    public void cambiar_datos(){
        SharedPreferences shared = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String nombre = (shared.getString("nombre", ""));
        String correo = (shared.getString("correo", ""));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView nav_user = (TextView) hView.findViewById(R.id.nombre_menu);
        nav_user.setText(nombre);

        TextView nav_correo = (TextView) hView.findViewById(R.id.correo_menu);
        nav_correo.setText(correo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}