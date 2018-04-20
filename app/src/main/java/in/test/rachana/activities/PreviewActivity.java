package  in.test.rachana.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import in.test.rachana.R;
import in.test.rachana.UtilsApp.GlobalUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private String imgPath = "";
    private ImageView ivPreview;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imgPath = getIntent().getStringExtra("image_path");

        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        initImageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

            } else {
                requestPermission();
            }
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void initImageView() {
        ivPreview = (ImageView) findViewById(R.id.ivPreview);

        if (!imgPath.isEmpty()) {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivPreview.setImageBitmap(bitmap);
            }
        }
    }

    public String imageToPDF(Bitmap bitmap) throws FileNotFoundException {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String filePath = Environment.getExternalStorageDirectory() + File.separator + "UKPaystub/pdf/" + GlobalUtils.timeStampString() + ".pdf";
        File f = new File(filePath);
        try {
            document.writeTo(new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            return "";
        }

        // close the document
        document.close();
        return filePath;
    }

    private void shareViaEmail(String pdfPath) {
        Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
        emailSelectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"address@mail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        emailIntent.setSelector( emailSelectorIntent );

        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pdfPath)));

        startActivity(emailIntent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    requestPermission();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!imgPath.isEmpty()) {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                imgFile.delete();
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (bitmap != null) {
                    try {
                        String pdfPath = imageToPDF(bitmap);
                        if (!pdfPath.isEmpty()) {
                            Log.e("PdfPath", pdfPath);
                            shareViaEmail(pdfPath);
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
}
