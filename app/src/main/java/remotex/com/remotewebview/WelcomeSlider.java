package remotex.com.remotewebview;

import static remotex.com.remotewebview.constants.jsonUrl;
import static remotex.com.remotewebview.constants.screen1BgColor;
import static remotex.com.remotewebview.constants.screen1Desc;
import static remotex.com.remotewebview.constants.screen1Img;
import static remotex.com.remotewebview.constants.screen1TextColor;
import static remotex.com.remotewebview.constants.screen1TitleText;
import static remotex.com.remotewebview.constants.screen2BgColor;
import static remotex.com.remotewebview.constants.screen2Desc;
import static remotex.com.remotewebview.constants.screen2Img;
import static remotex.com.remotewebview.constants.screen2TextColor;
import static remotex.com.remotewebview.constants.screen2TitleText;
import static remotex.com.remotewebview.constants.screen3BgColor;
import static remotex.com.remotewebview.constants.screen3Desc;
import static remotex.com.remotewebview.constants.screen3Img;
import static remotex.com.remotewebview.constants.screen3TextColor;
import static remotex.com.remotewebview.constants.screen3TitleText;
import static remotex.com.remotewebview.constants.screen4BgColor;
import static remotex.com.remotewebview.constants.screen4Desc;
import static remotex.com.remotewebview.constants.screen4Img;
import static remotex.com.remotewebview.constants.screen4TextColor;
import static remotex.com.remotewebview.constants.screen4TitleText;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import remotex.com.remotewebview.glidetovectoryou.GlideToVectorYou;
import remotex.com.remotewebview.glidetovectoryou.GlideToVectorYouListener;


public class WelcomeSlider extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private remotex.com.remotewebview.prefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new prefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View yourView = inflater.inflate(R.layout.slider_layout_1, , false);
//        screen1bg = yourView.findViewById(R.id.slider1layout);


//        screen1bg = ((findViewById(R.id.slider1layout)));
//
//        if (!(screen1BgColor == null)) {
//            screen1bg.setBackgroundColor(Color.parseColor(screen1BgColor));
//        }
        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.slider_layout_1,
                R.layout.slider_layout_2,
                R.layout.slider_layout_3,
                R.layout.slider_layout_4

        };


        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {

        if (jsonUrl == null) {
            Intent intent = new Intent(WelcomeSlider.this, Splash.class);
            startActivity(intent);
            finish();
        }

        super.onResume();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeSlider.this, WebActivity.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * View pager adapter
     */

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);


            if (position == 0) {
                ImageView img = view.findViewById(R.id.slider1_img);
                loadImg(img, screen1Img);

                TextView txt = view.findViewById(R.id.slider1title);
                TextView txt2 = view.findViewById(R.id.slider1desc);
                txt.setTextColor(Color.parseColor(screen1TextColor));
                txt2.setTextColor(Color.parseColor(screen1TextColor));
                txt.setText(screen1TitleText);
                txt2.setText(screen1Desc);


                if (!(screen1BgColor == null)) {
                    view.setBackgroundColor(Color.parseColor(screen1BgColor));
                }
            } else if (position == 1) {
//                screen2bg = findViewById(R.id.slider2layout);
                ImageView img = view.findViewById(R.id.slider2_img);
                loadImg(img, screen2Img);

                TextView txt = view.findViewById(R.id.slider2title);
                TextView txt2 = view.findViewById(R.id.slider2desc);

                txt.setTextColor(Color.parseColor(screen2TextColor));
                txt2.setTextColor(Color.parseColor(screen2TextColor));
                txt.setText(screen2TitleText);
                txt2.setText(screen2Desc);
                if (!(screen2BgColor == null)) {
                    view.setBackgroundColor(Color.parseColor(screen2BgColor));

                }
            } else if (position == 2) {

                ImageView img = view.findViewById(R.id.slider3_img);
                loadImg(img, screen3Img);

                TextView txt = view.findViewById(R.id.slider3title);
                TextView txt2 = view.findViewById(R.id.slider3desc);
                txt.setTextColor(Color.parseColor(screen3TextColor));
                txt2.setTextColor(Color.parseColor(screen3TextColor));
                txt.setText(screen3TitleText);
                txt2.setText(screen3Desc);
                if (!(screen3BgColor == null)) {
                    view.setBackgroundColor(Color.parseColor(screen3BgColor));
                }
            } else if (position == 3) {

                ImageView img = view.findViewById(R.id.slider4_img);
                loadImg(img, screen4Img);

                TextView txt = view.findViewById(R.id.slider4title);
                TextView txt2 = view.findViewById(R.id.slider4desc);
                txt.setTextColor(Color.parseColor(screen4TextColor));
                txt2.setTextColor(Color.parseColor(screen4TextColor));
                txt.setText(screen4TitleText);
                txt2.setText(screen4Desc);

                if (!(screen4BgColor == null)) {
                    view.setBackgroundColor(Color.parseColor(screen4BgColor));
                }
            }

            container.addView(view);


//            if (!(screen1BgColor == null)) {
//                screen1bg.setBackgroundColor(Color.parseColor(screen1BgColor));
//            }
//            int currpos = getItemPosition(0);
            int currpos = getItemPosition(position);
//            try {
//
//
//            if ( position==1 | position==0) {
////                screen1bg = findViewById(R.id.slider1layout);
//                if (!(screen1BgColor == null)) {
//                    view.setBackgroundColor(Color.parseColor(screen1BgColor));
//                }
//            }
//                else if (position == 2) {
////                screen2bg = findViewById(R.id.slider2layout);
//                if (!(screen2BgColor == null)) {
//                    view.setBackgroundColor(Color.parseColor(screen2BgColor));
//                }
//            }
//            } else if (position == 3){
//                screen3bg = layoutInflater.
//                if (!(screen3BgColor == null)) {
//                    screen1bg.setBackgroundColor(Color.parseColor(screen3BgColor));
//                }
//                } else if (position == 4) {
//                screen4bg = findViewById(R.id.slider4layout);
//                if (!(screen4BgColor == null)) {
//                    screen1bg.setBackgroundColor(Color.parseColor(screen4BgColor));
//                }
//            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            screen2bg = findViewById(R.id.slider1layout);
//            screen3bg = findViewById(R.id.slider1layout);
//            screen3bg = findViewById(R.id.slider1layout);


            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        public void loadImg(ImageView view, String url) {


            if (url.endsWith("svg")) {
                GlideToVectorYou
                        .init()
                        .with(WelcomeSlider.this)
                        .withListener(new GlideToVectorYouListener() {
                            @Override
                            public void onLoadFailed() {
                            }

                            @Override
                            public void onResourceReady() {
                            }
                        })
                        .setPlaceHolder(R.drawable.demo_btn_24, R.drawable.demo_btn_24)

                        .load(Uri.parse(url), view);

            } else {
                Glide.with(WelcomeSlider.this)
                        .load(url) // image url
                        .placeholder(R.drawable.demo_btn_24) // any placeholder to load at start
                        .error(R.drawable.demo_btn_24)  // any image in case of error
                        .into(view);  // imageview object
            }


        }
    }
}
