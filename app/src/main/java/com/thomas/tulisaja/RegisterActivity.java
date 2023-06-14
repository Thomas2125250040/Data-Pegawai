//08
package com.thomas.tulisaja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.thomas.tulisaja.databinding.ActivityRegisterBinding;

import retrofit2.Call; //retrofit call
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                String konfirmasiPassword = binding.etKonfirmasiPasword.getText().toString();
                
                boolean bolehRegister = true;
                if (TextUtils.isEmpty(username)){
                    bolehRegister = false;
                    binding.etUsername.setError("Username tidak boleh Kosong!");
                }
                if (TextUtils.isEmpty(password)){
                    bolehRegister = false;
                    binding.etPassword.setError("Password tidak boleh Kosong!");
                }
                if (TextUtils.isEmpty(konfirmasiPassword)){
                    bolehRegister = false;
                    binding.etKonfirmasiPasword.setError("Konfirmasi Password tidak boleh Kosong!");
                }
                if (!password.equals(konfirmasiPassword)){
                    bolehRegister = false;
                    binding.etPassword.setError("Konfirmasi Passsword Tidak Sama");
                }
                if (password.length()<6){
                    bolehRegister = false;
                    binding.etPassword.setError("Password minimal 6 Karakter");
                }
                if (bolehRegister){
                    register(username,password);
                }
                
            }

        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void register(String username, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);
        //memanggil API Register
        APIService apiregister = Utility.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = apiregister.register("dirumahaja",username,password);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code()==200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1 ){
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        Utility.setValue(RegisterActivity.this,"xUsername",username);
                        Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Response = "+response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error : "+ t.getMessage());
                Toast.makeText(RegisterActivity.this, "Retrofit Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}