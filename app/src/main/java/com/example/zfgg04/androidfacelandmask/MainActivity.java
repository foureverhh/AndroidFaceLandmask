package com.example.zfgg04.androidfacelandmask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_process;
    ImageView imageView;
    Bitmap scope;
    Bitmap heart;
    Canvas canvas;
    Paint rectPaint = new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_process = findViewById(R.id.btn_process);
        imageView = findViewById(R.id.image_view);

        final Bitmap bitmapTom = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.tom);
        imageView.setImageBitmap(bitmapTom);

        scope = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.scope);
        heart = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.heart);

        final Bitmap tempBitmap = Bitmap.createBitmap(bitmapTom.getWidth(),bitmapTom.getHeight(),Bitmap.Config.RGB_565);
        canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmapTom,0,0,null);

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if(!faceDetector.isOperational()){
                    Toast.makeText(MainActivity.this,"FaceDetector is not available",Toast.LENGTH_LONG).show();
                }
                //Build frame to load bitmapTom
                Frame frame = new Frame.Builder().setBitmap(bitmapTom).build();
                //Store result from face detector
                SparseArray<Face> faces = faceDetector.detect(frame);

                for(int i=0;i<faces.size();i++){
                    Face face = faces.valueAt(i);
                    detectLandMarks(face);
                    //Scope on face
                    canvas.drawBitmap(scope,0,0,null);
                    
                }

            }
        });
    }

    private void detectLandMarks(Face face) {
        for(Landmark landmark : face.getLandmarks()){
            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            drawOnImageView(landmark.getType(),cx,cy);
    }
}

    private void drawOnImageView(int type, int cx, int cy) {
        //Draw heart on mouth
        if(type == Landmark.BOTTOM_MOUTH){
            int scaleWidth = heart.getScaledWidth(canvas);
            int scaleHeight = heart.getScaledHeight(canvas);
            canvas.drawBitmap(heart,cx-scaleWidth/2,cy-scaleHeight*2,null);
          /*
            //Draw scope
            canvas.drawBitmap(scope,cx-500,cy-scaleHeight+120,null);
            */
        }
    }
    }
