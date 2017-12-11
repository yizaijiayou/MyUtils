package com.example.myutils.utils.scan.zxing;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.myutils.R;
import com.example.myutils.utils.scan.zxing.android.BeepManager;
import com.example.myutils.utils.scan.zxing.android.CaptureActivityHandler;
import com.example.myutils.utils.scan.zxing.android.FinishListener;
import com.example.myutils.utils.scan.zxing.android.InactivityTimer;
import com.example.myutils.utils.scan.zxing.android.IntentSource;
import com.example.myutils.utils.scan.zxing.camera.CameraManager;
import com.example.myutils.utils.scan.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * 使用方法（1.添加清单文件<activity android:name=".utils.scan.zxing.CaptureActivity"/>；2.使用前请先添加权限： WRITE_EXTERNAL_STORAGE  和  CAMERA ）
 * 1.调用相机扫描，可以扫描二维码。也可以扫描条形码
 * Intent intent = new Intent(this,CaptureActivity.class);
 * startActivityForResult(intent, CaptureActivity.SCANNING_CODE);
 * //回调方法看下面
 * 2.生成二维码
 * Bitmap qrCode = CodeCreator.createQRCode("123");
 * imageView.setImageBitmap(qrCode);
 */

 /*
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  // 扫描二维码/条码回传
  if (requestCode == CaptureActivity.SCANNING_CODE && resultCode == RESULT_OK) {
  if (data != null) {

  String content = data.getStringExtra(CaptureActivity.DECODED_CONTENT_KEY);
  Bitmap bitmap = data.getParcelableExtra(CaptureActivity.DECODED_BITMAP_KEY);

  textView.setText("" + content);
  }}}
 */

public final class CaptureActivity extends Activity implements
        SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final String DECODED_CONTENT_KEY = "codedContent";      //返回扫描之后的内容
    public static final String DECODED_BITMAP_KEY = "codedBitmap";        //返回扫描之后的二维码图像
    public static final int SCANNING_CODE = 10087;                        //调用相机扫描的数值
    //
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    //
    private InactivityTimer inactivityTimer;
    //
    private BeepManager beepManager;

    private ImageButton imageButton_back;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);
        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        imageButton_back = (ImageButton) findViewById(R.id.capture_imageview_back);
        imageButton_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            beepManager.playBeepSoundAndVibrate();

            Intent intent = getIntent();
            intent.putExtra("codedContent", rawResult.getText());
            intent.putExtra("codedBitmap", barcode);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    /**
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 显示底层错误信息并退出应用
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

}
