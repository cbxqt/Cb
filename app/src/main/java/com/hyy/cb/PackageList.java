package com.hyy.cb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PackageList extends AppCompatActivity {
    private List<String> listData;
    private ArrayAdapter<String> arrayAdapter;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);

        sharedPreferences = getSharedPreferences("CbPreferences", MODE_PRIVATE);
        listData = new ArrayList<>(sharedPreferences.getStringSet("packageList", new HashSet<String>()));

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.black)); // 设置文字颜色为黑色
                return view;
            }
        };

        ListView listView = findViewById(R.id.list_packageName);
        listView.setAdapter(arrayAdapter);

        //长按删除包名
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        Button addbutton = findViewById(R.id.add_package_button);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });
    }

    private void addNewItem() {
        EditText editText = findViewById(R.id.add_package_edittext);
        String newItem = editText.getText().toString().trim();
        if (!newItem.isEmpty()) {
            listData.add(newItem);
            sharedPreferences.edit().putStringSet("packageList", new HashSet<String>(listData)).apply();

            arrayAdapter.notifyDataSetChanged();
            editText.setText("");
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请输入有效内容", Toast.LENGTH_SHORT).show();
        }
    }
    // 长按删除的对话框
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("确认删除");
        builder.setMessage("是否要删除该项？");

        // 添加确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDeleteConfirmed(position);
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 添加取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击取消，不执行任何操作
            }
        });

        // 显示对话框
        builder.create().show();
    }

    // 删除确认
    public void onDeleteConfirmed(int position) {
        listData.remove(position);
        sharedPreferences.edit().putStringSet("packageList", new HashSet<String>(listData)).apply();
        arrayAdapter.notifyDataSetChanged();
    }

}
