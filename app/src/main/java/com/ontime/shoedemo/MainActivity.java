package com.ontime.shoedemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    //鞋子平移动画
    private ScaleAnimation scaleAnimation;
    //鞋子动画集
    private AnimationSet animationSet;
    private TextView tvPlace;
    //鞋子平移动画
    private TranslateAnimation translateAnimation;

    private ImageView ivShoe;

    private ImageView ivPhone;
    //手机渐现动画
    private AlphaAnimation alphaAnimation;
    //弹窗
    private AlertDialog dialog;
    //弹窗需要渲染的view
    private View inflateView;
    private EditText etDialogView;
    //视频布局
    private RelativeLayout videoLayout;
    //静态鞋子布局
    private LinearLayout showLayout;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        tvPlace = (TextView) findViewById(R.id.tvPlace);
        tvPlace.setText("<--Place your phone here to order");

        ivShoe = (ImageView) findViewById(R.id.ivShoe);

        ivPhone = (ImageView) findViewById(R.id.ivPhone);

        videoView = (VideoView) findViewById(R.id.videoView);

        videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
        showLayout = (LinearLayout) findViewById(R.id.showLayout);
        showLayout.setVisibility(View.INVISIBLE);
        //手机暂不可见
        ivPhone.setVisibility(View.INVISIBLE);
        //获得窗口管理器进而获得屏幕宽度和高度
        WindowManager manager = this.getWindowManager();
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        Log.i("Width",width+"");
        Log.i("Height",height+"");

        //视频资源uri
        Uri uri = Uri.parse( "android.resource://com.ontime.shoedemo/"+R.raw.nike );

        videoView = (VideoView)this.findViewById(R.id.videoView );

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                
                videoLayout.setVisibility(View.INVISIBLE);
                showLayout.setVisibility(View.VISIBLE);
            }
        });

        //设置视频路径
        videoView.setVideoURI(uri);
        //去除边框，全屏播放视频
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);

        //开始播放视频
        videoView.start();

    }



    public void click(View view){
        //先将手机图片设为可见的
        ivPhone.setVisibility(View.VISIBLE);

        //初始化动画集
        animationSet = new AnimationSet(false);

        //鞋子缩放动画设置
        scaleAnimation = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f);
        //缩放动画持续时长
        scaleAnimation.setDuration(3000);
        //将缩放动画添加至动画集中
        animationSet.addAnimation(scaleAnimation);

        //加载平移动画
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.translate);
        //设置平移动画时长
        animation.setDuration(3000);
        //将平移动画加入至动画集中
        animationSet.addAnimation(animation);

        //为手机设置透明度渐现动画
        alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(2000);
        ivPhone.startAnimation(alphaAnimation);

        //animationSet.setFillAfter(true);
        //animationSet.setFillBefore(true);
        ivShoe.startAnimation(animationSet);
        //监听动画集的播放
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //在动画播放完毕后手机图案设置为不可见
                ivPhone.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    //透明按钮点击弹窗
    public void showDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        inflateView = LayoutInflater.from(this).inflate(R.layout.dialog_view,null);
        etDialogView = (EditText) view.findViewById(R.id.etDeviceID);
        builder.setTitle("请输入设备ID:")
                .setView(inflateView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "input success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

}
