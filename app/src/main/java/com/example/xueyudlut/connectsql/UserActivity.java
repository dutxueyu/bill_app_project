package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class UserActivity extends Activity implements View.OnClickListener{
    private EditText et_bill_code;
    private EditText et_bill_num;
    private EditText et_bill_date;
    private EditText et_bill_money;
    private Button btn_upload;
    private Button btn_getbillimage;
    private DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    public static final int REQUEST_CODE = 111;
    public static final int REQUEST_Image_CODE = 101;
    private  String SDpath;
    private  String ImagePath;
    private  String smallImgPath;
    private  boolean hasGotImage = false;
    //处理消息
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1)
                et_bill_num.setError("该发票数据已存在！");
            if (msg.what==0)
                Toast.makeText(getApplicationContext(),"数据提交成功！",Toast.LENGTH_LONG).show();
            btn_upload.setClickable(true);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        et_bill_code = findViewById(R.id.editText_billcode);
        et_bill_num = findViewById(R.id.editText_billnum);
        et_bill_date = findViewById(R.id.editText_date);
        et_bill_money = findViewById(R.id.editText_billmoney);
        btn_upload = findViewById(R.id.btn_UpLoadData);
        btn_getbillimage = findViewById(R.id.btn_getbillimage);
        findViewById(R.id.btn_UpLoadData).setOnClickListener(this);
        findViewById(R.id.btn_QR).setOnClickListener(this);
        btn_getbillimage.setOnClickListener(this);
        //照片存储路径
        SDpath = Environment.getExternalStorageDirectory().getPath()+ File.separator+"Android/data/"+getPackageName()+"files";
        ImagePath = SDpath+"/bill_image.png";
        smallImgPath =SDpath+"/"+"small_img.jpg";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;}
                if (bundle.getInt(CodeUtils.RESULT_TYPE)
                        == CodeUtils.RESULT_SUCCESS) {
                    String result =
                            bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    String []message = result.split(",");
                    et_bill_code.setText(message[2]);
                    et_bill_num.setText(message[3]);
                    et_bill_money.setText(message[4]);
                    et_bill_date.setText(message[5]);
                }
            }
        }
        if ( resultCode ==Activity.RESULT_OK &&requestCode ==REQUEST_Image_CODE)
        {
            Bitmap bm = getSmallBitmap(ImagePath);
            int degree = readPictureDegree(ImagePath);
            if(degree!=0)
                bm = rotateBitmap(bm,degree);
            File  f  = new File(SDpath+"/","small_img.jpg");
            if(f.exists())
                f.delete();
            try {
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG,90,out);
                out.flush();
                out.close();
                Toast.makeText(getApplicationContext(),"image finished!",Toast.LENGTH_LONG).show();
                hasGotImage = true;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_QR:
                startActivityForResult(new Intent(UserActivity.this,CaptureActivity.class),REQUEST_CODE);
                break;
            case R.id.btn_UpLoadData:
                String billcode =  et_bill_code.getText().toString();
                String billnum = et_bill_num.getText().toString();
                String billdata = et_bill_date.getText().toString();
                String billmoney = et_bill_money.getText().toString();
                if(billcode.equals(null)||billcode.equals("")){
                    et_bill_code.setError("此项内容不能为空！");
                    Toast.makeText(getApplicationContext(),"必填内容不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (billnum.equals(null)||billnum.equals("")){
                    et_bill_num.setError(" 此项内容不能为空！");
                    Toast.makeText(getApplicationContext(),"必填内容不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (billdata.equals(null)||billdata.equals("")){
                    et_bill_date.setError("此项内容不能为空!");
                    Toast.makeText(getApplicationContext(),"必填内容不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (billmoney.equals(null)||billmoney.equals("")){
                    et_bill_money.setError("此项内容不能为空!");
                    Toast.makeText(getApplicationContext(),"必填内容不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!hasGotImage){
                    Toast.makeText(getApplicationContext(),"还未拍摄发票照片！",Toast.LENGTH_SHORT).show();
                    return;

                }
                btn_upload.setClickable(false);
                new Thread(new Runnable() {
                    String billcode =  et_bill_code.getText().toString();
                    String billnum = et_bill_num.getText().toString();
                    String billdata = et_bill_date.getText().toString();
                    String billmoney = et_bill_money.getText().toString();
                    int uid = MainActivity.main_uid;
                    @Override
                    public void run() {
                        String selectsql ="USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_Message] where [bill_num]='"+billnum+"'";
//                        String insertsql ="USE [bill_data] INSERT INTO [dbo].[T_Message] ([int_uid],[bill_code],[bill_num],[bill_date],[bill_moneynum],[is_checked]"+
//                                ")Values ('"+uid+"','"+billcode+"','"+billnum+"','"+billdata+"','"+billmoney+"','0')";
                        String insertsql ="USE [bill_data] INSERT INTO [dbo].[T_Message] ([int_uid],[bill_code],[bill_num],[bill_date],[bill_moneynum],[is_checked]"+
                                ")Values ('?','?','?','?','?','0')";
                        if (databaseHandle.SelectSQL(selectsql))
                            handler.sendEmptyMessage(1);
                        else
                            {
                                int t =  databaseHandle.insertwithImage(uid,billcode,billnum,billdata,billmoney,smallImgPath);
                                handler.sendEmptyMessage(t-1);
                        }
                    }
                }).start();
                break;
            case R.id.btn_getbillimage:
                getImageFromCamera();
                break;

        }
    }

    private void getImageFromCamera() {
        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        File dir = new File(SDpath);
        boolean t =dir.mkdirs();
        if(Build.VERSION.SDK_INT>=24)
        {
            File g= new File(ImagePath);//测试错误
            try {
                g.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uri = FileProvider.getUriForFile(this,"xueyu404",g);
        }else{
            uri = Uri.fromFile(new File(ImagePath));
        }
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(getImageByCamera,REQUEST_Image_CODE);
    }
    public  static  Bitmap getSmallBitmap(String  filePath){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        options.inSampleSize = calculateInSampleSize(options,400,400);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath,options);
    }
    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        System.out.println("bili:"+inSampleSize +"gag:"+height+"kuan"+width);
        return inSampleSize;
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 99, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

}
