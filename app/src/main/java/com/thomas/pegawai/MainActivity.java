//8
package com.thomas.pegawai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.thomas.pegawai.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private List<Post> data;
    private PostViewAdapter postViewAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!Utility.checkValue(this,"xUsername")){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();

        }

        postViewAdapter = new PostViewAdapter();
        binding.rvPost.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPost.setAdapter(postViewAdapter);
        postViewAdapter.setOnItemLongClickListener(new PostViewAdapter.OnItemLongClickListener() { //updatePost
            @Override
            public void onItemLongClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.setGravity(Gravity.RIGHT);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int idMenu = item.getItemId();

                            if(idMenu == R.id.action_edit) {
                                Intent intent = new Intent(MainActivity.this, UpdatePostActivity.class);
                                intent.putExtra("EXTRA_DATA", data.get(position));
                                startActivity(intent);
                                return true;
                            }else if (idMenu == R.id.action_delete) {
                                String id = data.get(position).getId();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                alertDialogBuilder.setTitle("Konfirmasi");
                                alertDialogBuilder.setMessage("Yakin ingin menghapus postingan '" + data.get(position).getNama()+ "' ? ");
                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deletePost(id);
                                        getAllPost();
                                    }


                                });
                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                return true;
                            }else {
                                return false;
                            }
                    }
                });
                popupMenu.show();
            }
        });

        binding.fabInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddPostActivity.class);
                startActivity(intent);
            }
        });


    }


    private void getAllPost(){
        binding.progressBar.setVisibility(View.VISIBLE);
        APIService api = Utility.getRetrofit().create(APIService.class);
        Call<ValueData<List<Post>>> call = api.getPost();
        call.enqueue(new Callback<ValueData<List<Post>>>() {
            @Override
            public void onResponse(Call<ValueData<List<Post>>> call, Response<ValueData<List<Post>>> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    int success = response.body().getSuccessNah();
                    String message = response.body().getMessage();
                    if (success == 1) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        data = response.body().getData();
                        postViewAdapter.setData(data);
                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Response" + response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ValueData<List<Post>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : " + t.getMessage());
                Toast.makeText(MainActivity.this, "Retrofit Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deletePost(String id) { //updatePost
        APIService api = Utility.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = api.deletePost(id);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if(response.code()==200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success == 1){
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        getAllPost();
                    }else{
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Response "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                System.out.println("Retrofit Errror : "+ t.getMessage());
                Toast.makeText(MainActivity.this, "Retrofit Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllPost();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            Utility.clearUser(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}