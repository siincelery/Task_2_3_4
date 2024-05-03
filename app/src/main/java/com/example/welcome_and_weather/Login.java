package com.example.welcome_and_weather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class Login extends Fragment {


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);
            EditText email = view.findViewById(R.id.Username);
            EditText password = view.findViewById(R.id.Password);
            Button sign_up = view.findViewById(R.id.sign_up);

            sign_up.setOnClickListener(v -> {
                String emails = email.getText().toString();
                String pass = password.getText().toString();

                if (emails.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(getActivity(), "Заполни все поля!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DBHelper dbHelper = new DBHelper(getActivity());
                SQLiteDatabase myDB = dbHelper.getWritableDatabase();

                Cursor cursor = myDB.query(DBHelper.TABLE_NAME,
                        null, DBHelper.COLUMN_EMAIL + "= ? AND " + DBHelper.COLUMN_PASSWORD + "= ?",
                        new String[]{emails, pass},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    Toast.makeText(getActivity(), "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Home.class);
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                myDB.close();
            });
            return view;
        }
}

