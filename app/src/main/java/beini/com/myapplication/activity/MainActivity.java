package beini.com.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import java.util.Date;
import beini.com.myapplication.R;
import beini.com.myapplication.adapter.BaseBean;
import beini.com.myapplication.adapter.FileAdapter;
import beini.com.myapplication.bean.SharedFileBean;
import beini.com.myapplication.server.FileHTTPServer;
import beini.com.myapplication.util.FileUtils;
import beini.com.myapplication.util.IpUtil;

/**
 * Create by beini  2017/12/22
 */
public class MainActivity extends Activity {
    private final int FILE_SELECT_CODE = 0x111;
    private Uri uri = null;
    private RecyclerView recycle_view;
    private FileAdapter fileAdapter;
    private FileHTTPServer fileServer;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initView();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, 1);
    }

    private void initView() {
        Button btn_choice = findViewById(R.id.btn_choice);
        Button btn_start_service = findViewById(R.id.btn_start_service);
        recycle_view = findViewById(R.id.recycle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view.setLayoutManager(linearLayoutManager);
        fileAdapter = new FileAdapter(new BaseBean<>(R.layout.item, FileHTTPServer.fileLists));
        recycle_view.setAdapter(fileAdapter);
        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fileServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        try {
            if (fileServer != null) {
                fileServer.start();
            } else {
                fileServer = new FileHTTPServer();
                fileServer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ip = IpUtil.getDeviceIp(this);
        String url = "点击下面按钮选择文件或者在系统文件夹内点击分享选择本应用，然后在局域网的浏览器内输入http://" + ip + ":8080";
        btn_start_service.setText(url);
        //
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                Uri uri2 = extras.getParcelable(Intent.EXTRA_STREAM);
                SharedFileBean sharedFile = returnShareFile(uri2);
                String fileName = sharedFile.getFileName();
                if (!FileHTTPServer.fileLists.contains(fileName)) {
                    FileHTTPServer.fileLists.add(sharedFile);
                }
                fileAdapter.notifyDataSetChanged();
            }
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//任意文件
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //调用系统的文件选择器
        startActivityForResult(Intent.createChooser(intent, "请选择分享的文件"), FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    SharedFileBean sharedFile = returnShareFile(uri);
                    String fileName = sharedFile.getFileName();
                    if (!FileHTTPServer.fileLists.contains(fileName)) {
                        FileHTTPServer.fileLists.add(sharedFile);
                    }
                    fileAdapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public SharedFileBean returnShareFile(Uri uri) {
        SharedFileBean sharedFile = new SharedFileBean();
        String filePath = FileUtils.getPath(this, uri);
        sharedFile.setPath(filePath);
        sharedFile.setFileName(FileUtils.path2Name(FileUtils.getPath(this, uri)));
        sharedFile.setTime(new Date().toString());
        return sharedFile;
    }
}

