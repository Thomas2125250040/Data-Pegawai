package com.thomas.pegawai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.thomas.pegawai.databinding.ActivityAddPostBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {
    private ActivityAddPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPostBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = binding.etNama.getText().toString().trim();
                String link = binding.etLink.getText().toString().trim();
                String umurku = binding.etUmur.getText().toString().trim();
                int umur=0;
                try {
                    umur = Integer.parseInt(binding.etUmur.getText().toString().trim());
                    if (umur > 100){
                        binding.tilUmur.setError("Harap masukkan umur yang masuk akal.");
                    }
                } catch (NumberFormatException nfe){
                    binding.tilUmur.setError("Umur harus di dalam format angka dan tidak boleh kosong.");
                }

                String jabatan = binding.etJabatan.getText().toString();
                if (TextUtils.isEmpty(link)){
                    binding.tilLink.setError("Link Gambar tidak boleh kosong!");
                } else if (TextUtils.isEmpty(nama)) {
                    binding.tilNama.setError("Nama tidak boleh kosong.");
                } else if (TextUtils.isEmpty(jabatan)){
                    binding.tilJabatan.setError("Jabatan tidak boleh kosong.");
                } else if (TextUtils.isEmpty(umurku)){
                    binding.tilUmur.setError("Umur tidak boleh kosong.");
                }
                else {
                    String keterangan = binding.etKeterangan.getText().toString();
                    addPost(link, nama, umur, jabatan, keterangan);
                }
            }


        });

    }
    private void addPost(String link, String nama, int umur, String jabatan, String keterangan) {
        binding.progressBar.setVisibility(View.VISIBLE);
        APIService api = Utility.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = api.addPost(link,nama,umur,jabatan,keterangan);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                binding.progressBar.setVisibility(View.GONE);
                if(response.code()==200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if(success==1){
                        Toast.makeText(AddPostActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddPostActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(AddPostActivity.this, "Response : "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : "+ t.getMessage());

                Toast.makeText(AddPostActivity.this, "Retrofit Erro : "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }
}