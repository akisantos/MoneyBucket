package com.akistd.moneybucket.ui.appintro;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

public class MoneyBucketIntro extends AppIntro {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.createInstance(
                "Xin chào! 😄",
                "MoneyBucket sẽ giúp bạn quản lý tài chính cá nhân một cách dễ dàng hơn với mô hình 6 hũ!",
                R.drawable.saly22,
                R.color.navg
        ));

        addSlide(AppIntroFragment.createInstance(
                "Thao tác đơn giản,\n dễ sử dụng",
                "Đăng nhập một chạm, khai báo thu chi nhanh chóng, tiện lợi.",
                R.drawable.saly12,
                R.color.xanhGood
        ));

        addSlide(AppIntroFragment.createInstance(
                "Lịch sử giao dịch rõ ràng",
                "Thống kê theo tuần tháng năm giúp bạn có góc nhìn tổng quan hơn về dòng tiền.",
                R.drawable.saly42,
                R.color.carrot
        ));

        addSlide(AppIntroFragment.createInstance(
                "Ôi thôi, quá trớn rồi!",
                "Thông báo khi dùng quá % số dư trong khoảng thời gian ngắn.",
                R.drawable.saly26,
                R.color.doWarning
        ));

        addSlide(AppIntroFragment.createInstance(
                "Truy cập muôn nơi!",
                "Đồng bộ liên tục mỗi khi có mạng.",
                R.drawable.saly1,
                R.color.peterriver
        ));

        addSlide(AppIntroFragment.createInstance(
                "Một xíu nữa thôi...",
                "App giúp bạn quản lý đơn giản hơn nhưng " +
                        "chính bạn là yếu tố then chốt mang lại thành quả.\n \n Nhóm xin chúc bạn có một đời sống \ntài chính bình an. 🥰",
                R.drawable.saly37,
                R.color.ame
        ));


        // Fade Transition
        setTransformer(new AppIntroPageTransformerType.Parallax());
        // Show/hide status bar
        showStatusBar(true);
        //Enable the color "fade" animation between two slides (make sure the slide implements SlideBackgroundColorHolder)
        setColorTransitionsEnabled(true);

        //Prevent the back button from exiting the slides
        setSystemBackButtonLocked(true);

        //Activate wizard mode (Some aesthetic changes)
        setWizardMode(true);


        //Enable/disable page indicators
        setIndicatorEnabled(true);

        //Dhow/hide ALL buttons
        setButtonsEnabled(true);

        this.isColorTransitionsEnabled();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

}
