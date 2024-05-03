package com.example.welcome_and_weather;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.Fragment;


public class Register extends Fragment {
    EditText email, password, YourName, conPasswordET;
    Button registerBtn;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_register, container, false);

            email = view.findViewById(R.id.emailET);
            password = view.findViewById(R.id.Password);
            YourName = view.findViewById(R.id.YourName);
            conPasswordET = view.findViewById(R.id.conPasswordET);
            registerBtn = view.findViewById(R.id.registerBtn);

            registerBtn.setOnClickListener(v -> {
                String ema = email.getText().toString();
                String pass = password.getText().toString();
                String username = YourName.getText().toString();
                String repass = conPasswordET.getText().toString();
                if (ema.isEmpty() || pass.isEmpty() || username.isEmpty() || repass.isEmpty()) {
                    Toast.makeText(getActivity(), "Заполни все поля!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pass.equals(repass)) {
                    Toast.makeText(getActivity(), "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHelper dbHelper = new DBHelper(getActivity());
                SQLiteDatabase myDB = dbHelper.getWritableDatabase();

                Cursor cursor = myDB.query(DBHelper.TABLE_NAME, null, DBHelper.COLUMN_EMAIL + " = ?", new String[]{ema}, null, null, null);

                if (cursor.moveToFirst()) {
                    Toast.makeText(getActivity(), "Вы уже зарегистрированы!!", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(DBHelper.COLUMN_EMAIL, ema);
                    values.put(DBHelper.COLUMN_USERNAME, username); // Исправлено на имя столбца
                    values.put(DBHelper.COLUMN_PASSWORD, pass);
                    myDB.insert(DBHelper.TABLE_NAME, null, values);
                    Toast.makeText(getActivity(), "Вы зарегистрировались!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Home.class);
                    intent.putExtra("email", ema);
                    startActivity(intent);
                }
                cursor.close();
                myDB.close();
            });
            return view;


        }
}
