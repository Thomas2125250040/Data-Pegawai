package com.thomas.pegawai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thomas.pegawai.databinding.ActivityUpdatePostBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePostActivity extends AppCompatActivity {

    private ActivityUpdatePostBinding binding;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        post = getIntent().getParcelableExtra("EXTRA_DATA");
        String id = post.getId();
        binding.etNama.setText(post.getNama());
        binding.etUmur.setText(Integer.toString(post.getUmur()));
        binding.etJabatan.setText(post.getJabatan());
        binding.etKeterangan.setText(post.getKeterangan());
        binding.etLink.setText(post.getGambar());
        Glide.with(UpdatePostActivity.this).load(post.getGambar())
                        .apply(new RequestOptions().override(200,200))
                                .into(binding.ivPegawai);
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                    updatePost(link, id, nama, umur, jabatan, keterangan);
                }
            }
        });
    }
    private void updatePost(String link, String id, String nama, int umur, String jabatan, String keterangan) {
        binding.progressBar.setVisibility(View.VISIBLE);
        APIService api = Utility.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = api.updatePost(link, id,nama,umur,jabatan,keterangan);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code()==200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success==1){
                        Toast.makeText(UpdatePostActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdatePostActivity.this,MainActivity.class); //sdh post lngsg pindah
                        finish();
                        startActivity(intent);
                    }else {
                        Toast.makeText(UpdatePostActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UpdatePostActivity.this, "Response : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : "+ t.getMessage());
                Toast.makeText(UpdatePostActivity.this, "Retrofit Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }
}