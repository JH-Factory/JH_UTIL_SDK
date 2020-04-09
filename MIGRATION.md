# Migration Guide (for Android)

구 SDK를 사용 중이던 매체사에서는 해당 마이그레이션 가이드를 참고하면 쉽게 신규 SDK를 적용할 수 있습니다.

## 목차

1. [공통 변경 사항](#1-공통-변경-사항)
2. [전면 광고 (Interstitial Ad) 마이그레이션](#2-전면-광고-interstitial-ad-마이그레이션)
   * [구 SDK 사용 방법](#구-sdk-사용-방법)
   * [신규 SDK 사용 방법](#신규-sdk-사용-방법)
   * [차이점](#차이점)
3. [배너 광고 (Banner Ad) 마이그레이션](#3-배너-광고-banner-ad-마이그레이션)
   * [XML 뷰 삽입 방식](#xml-뷰-삽입-방식)
     * [구 SDK 사용 방법](#구-sdk-사용-방법-1)
     * [신규 SDK 사용 방법](#신규-sdk-사용-방법-1)
     * [차이점](#차이점-1)
   * [뷰 동적 생성 방식](#뷰-동적-생성-방식)
     * [구 SDK 사용 방법](#구-sdk-사용-방법-2)
     * [신규 SDK 사용 방법](#신규-sdk-사용-방법-2)
     * [차이점](#차이점-2)
4. [네이티브 광고 (Native Ad) 마이그레이션](#4-네이티브-광고-native-ad-마이그레이션)
   * [구 SDK 사용 방법](#구-sdk-사용-방법-3)
   * [신규 SDK 사용 방법](#신규-sdk-사용-방법-3)
   * [차이점](#차이점-3)
5. [동영상 광고 (Video Ad)](#5-동영상-광고-video-ad)
   * [구 SDK 사용 방법](#구-sdk-사용-방법-4)
   * [신규 SDK 사용 방법](#신규-sdk-사용-방법-4)
   * [차이점](#차이점-4)

## 1. 공통 변경 사항

1) **App ID**의 명칭을 **Inventory ID**로 변경하였습니다.

2) **광고 로직 ID**의 명칭을 **Placement ID**로 변경하였습니다.

* Placement ID는 필수입니다. 
* 구 SDK에서 광고 로직 ID 설정없이 사용하던 광고의 경우 Placement ID를 생성하고 광고 타입에 맞는 설정 후 이용해주세요.

3) 신규 SDK에서 모든 광고 리스너는 **AdListener**로 통합되어 사용됩니다.

4) 광고 리스너의 onFailure에서 제공되는 **AdError 클래스**를 사용하여 에러의 원인 파악이 쉬워졌습니다.



## 2. 전면 광고 (Interstitial Ad) 마이그레이션

구 SDK에서 신규 SDK로 전면 광고를 마이그레이션 하기 위해 사용 방법을 비교해보면 아래와 같습니다.

### 구 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	...
    
    TnkSession.prepareInterstitialAd(this, "Logic ID", new TnkAdListener() {
        @Override
        public void onClose(int type) {
            // 종료 버튼 선택 시 앱을 종료합니다.
            if (type == TnkAdListener.CLOSE_EXIT) {
                finish();
            }
        }

        @Override
        public void onFailure(int errCode) {
            Log.e("Test", "Error : " + errCode);
        }

        @Override
        public void onLoad() {
        }

        @Override
        public void onShow() {
        }
    });

    TnkSession.showInterstitialAd(this);
  
  ...
}
```

### 신규 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...
    
    final InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this, "Placement ID");
    interstitialAdItem.setListener(new AdListener() {
        @Override
        public void onClose(AdItem adItem, int type) {
            // 종료 버튼 선택 시 앱을 종료합니다.
            if (type == AdListener.CLOSE_EXIT) {
                finish();
            }
        }

        @Override
        public void onError(AdItem adItem, AdError error) {
            Log.e("Test", "Error : " + error.getMessage());
        }

        @Override
        public void onLoad(AdItem adItem) {
            // 광고가 로드 완료된 후 show를 호출해주어야 합니다.
            interstitialAdItem.show();
        }

        @Override
        public void onShow(AdItem adItem) {
        }

        @Override
        public void onClick(AdItem adItem) {
        }
    });

    interstitialAdItem.load();
  
  ...
}
```

### 차이점

1) **InterstitialAdItem** 클래스가 추가되어 광고를 로드하고 노출할 수 있게 변경되었습니다.

2) 광고 리스너에 광고 클릭을 감지할 수 있는 **onClick** 추가되었습니다.

3) **show** 호출 시점을 광고 로드 완료 후로 바꾸었습니다.



## 3. 배너 광고 (Banner Ad) 마이그레이션

배너 광고를 사용하는 방법은 XML 뷰 삽입 방식과 뷰 동적 생성 방식 두 가지가 있습니다.

구 SDK에서 신규 SDK로 전면 광고를 마이그레이션 하기 위해 사용 방법을 비교해보면 아래와 같습니다.

### XML 뷰 삽입 방식

첫 번째로 XML 방식을 비교해 봅시다.

#### 구 SDK 사용 방법

##### XML 뷰 삽입

```xml
<com.tnkfactory.ad.BannerAdView
    android:id="@+id/banner_ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

##### 배너 광고 로드

```java
private BannerAdView bannerAdView;

@Override
protected void onCreate(Bundle savedInstanceState) {
	...
    
    bannerAdView = findViewById(R.id.banner_ad_view);
    bannerAdView.setBannerAdListener(new BannerAdListener() {
        @Override
        public void onFailure(int errCode) {
            Log.e("Test", "errCode : " + errCode);
        }

        @Override
        public void onShow() {
        }

        @Override
        public void onClick() {
        }
    });

    bannerAdView.loadAd("Logic ID");
  
  ...
}

@Override
protected void onPause() {
    if (bannerAdView != null) {
        bannerAdView.onPause();
    }
    
    super.onPause();
}

@Override
protected void onResume() {
    super.onResume();

    if (bannerAdView != null) {
        bannerAdView.onResume();
    }
}

@Override
protected void onDestroy() {
    if (bannerAdView != null) {
        bannerAdView.onDestroy();
    }
    
    super.onDestroy();
}
```

#### 신규 SDK 사용 방법

##### XML 뷰 삽입

```xml
<com.tnkfactory.ad.BannerAdView
		android:id="@+id/banner_ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:placement_id="Placement_ID" />
```

##### 배너 광고 로드

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...
    
    BannerAdView bannerAdView = findViewById(R.id.banner_ad_view);
    bannerAdView.setListener(new AdListener() {
        @Override
        public void onError(AdItem adItem, AdError error) {
            Log.e("Test", "errCode : " + error.getMessage());
        }

        @Override
        public void onLoad(AdItem adItem) {
        }

        @Override
        public void onShow(AdItem adItem) {
        }

        @Override
        public void onClick(AdItem adItem) {
        }
    });

    bannerAdView.load();
  
  ...
}
```

#### 차이점

1) XML에 BannerAdView 옵션으로 **placement_id**가 추가되어 광고 로드 시점이 아닌 뷰 삽입 시점에 입력하도록 변경되었습니다.

2) 광고 리스너에 광고 로드 완료를 감지할 수 있는 **onLoad** 추가되었습니다.

3) 생명주기 관리 함수가 삭제되고 **자동처리** 해주도록 변경되었습니다.

### 뷰 동적 생성 방식

#### 구 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	...
    
    RelativeLayout mainLayout = findViewById(R.id.main_layout);
    BannerAdView bannerAdView = new BannerAdView(this);
    mainLayout.addView(bannerAdView);

    bannerAdView.setBannerAdListener(new BannerAdListener() {
        @Override
        public void onFailure(int errCode) {
            Log.e("Test", "errCode : " + errCode);
        }

        @Override
        public void onShow() {
        }

        @Override
        public void onClick() {
        }
    });

    bannerAdView.loadAd("Logic ID");

  ...
}

@Override
protected void onPause() {
    if (bannerAdView != null) {
        bannerAdView.onPause();
    }

    super.onPause();
}

@Override
protected void onResume() {
    super.onResume();

    if (bannerAdView != null) {
        bannerAdView.onResume();
    }
}

@Override
protected void onDestroy() {
    if (bannerAdView != null) {
        bannerAdView.onDestroy();
    }

    super.onDestroy();
}

```



#### 신규 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	...
    
    RelativeLayout mainLayout = findViewById(R.id.main_layout);
    BannerAdView bannerAdView = new BannerAdView(this, "Placement ID");
    mainLayout.addView(bannerAdView);

    bannerAdView.setListener(new AdListener() {
        @Override
        public void onError(AdItem adItem, AdError error) {
            Log.e("Test", "errCode : " + error.getMessage());
        }

        @Override
        public void onLoad(AdItem adItem) {
        }

        @Override
        public void onShow(AdItem adItem) {
        }

        @Override
        public void onClick(AdItem adItem) {
        }
    });

    // 배너 광고 로드
    bannerAdView.load();
  
  ...
}
```

#### 차이점

1) Placement ID를 광고 로드 시점이 아닌 **BannerAdView 생성 시점**에 입력하도록 변경되었습니다.

2) 광고 리스너에 광고 로드 완료를 감지할 수 있는 **onLoad** 추가되었습니다.

3) 생명주기 관리 함수가 삭제되고 **자동처리** 해주도록 변경되었습니다.

## 4. 네이티브 광고 (Native Ad) 마이그레이션

### 구 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...

    NativeAdItem adItem = new NativeAdItem(this, NativeAdItem.STYLE_LANDSCAPE_1200, new NativeAdListener() {
        @Override
        public void onFailure(int errCode) {
            Log.d("Test", "errCode : " + errCode);
        }

        @Override
        public void onLoad(NativeAdItem adItem) {
            showNativeAd(adItem);
        }

        @Override
        public void onClick() {
        }

        @Override
        public void onShow() {
        }

    });

    adItem.prepareAd("Logic ID");
  
  ...
}

private void showNativeAd(NativeAdItem adItem) {
    ViewGroup adContainer = findViewById(R.id.native_ad_container);
    adContainer.removeAllViews();

    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    RelativeLayout adItemView = (RelativeLayout)inflater.inflate(R.layout.native_ad_item, null);

    ImageView adIcon = adItemView.findViewById(R.id.ad_icon);
    adIcon.setImageBitmap(adItem.getIconImage());

    TextView titleView = adItemView.findViewById(R.id.ad_title);
    titleView.setText(adItem.getTitle());

    TextView descView = adItemView.findViewById(R.id.ad_desc);
    descView.setText(adItem.getDescription());

    ImageView adImage = adItemView.findViewById(R.id.ad_image);
    adImage.setImageBitmap(adItem.getCoverImage());

    RelativeLayout watermarkContainer = adItemView.findViewById(R.id.watermark_container);
    ImageView watermark = adItem.getWatermark();
    watermarkContainer.addView(watermark);

    adContainer.addView(adItemView);

    adItem.attachLayout(adItemView);
}
```

### 신규 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	...
    
    NativeAdItem nativeAdItem = new NativeAdItem(this, "Placement ID");
    nativeAdItem.setListener(new AdListener() {
        @Override
        public void onError(AdItem adItem, AdError error) {
            Log.e("Test", "errCode : " + error.getMessage());
        }

        @Override
        public void onLoad(AdItem adItem) {
            showNativeAd((NativeAdItem) adItem);
        }

        @Override
        public void onShow(AdItem adItem) {
        }

        @Override
        public void onClick(AdItem adItem) {
        }
    });

    // 네이티브 광고 로드
    nativeAdItem.load();
  
  ...
}

// 네이티브 광고 노출
private void showNativeAd(NativeAdItem nativeAdItem) {

    if (nativeAdItem != null & nativeAdItem.isLoaded()) {

        // 네이티브 광고가 삽입될 컨테이너 초기화
        ViewGroup adContainer = findViewById(R.id.native_ad_container);
        adContainer.removeAllViews();

        // 네이티브 아이템 레이아웃 삽입
        ViewGroup view = (ViewGroup) View.inflate(this, R.layout.native_ad_item, adContainer);

        // 네이티브 바인더 객체 생성
        // 생성자에 메인 컨텐츠가 표시될 뷰 ID 필수 입력
        NativeViewBinder binder = new NativeViewBinder(R.id.native_ad_content);

        // 네이티브 바인더 셋팅
        binder.iconId(R.id.native_ad_icon)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_desc)
                .starRatingId(R.id.native_ad_rating)
                .watermarkIconId(R.id.native_ad_watermark_container)
                .addClickView(R.id.native_ad_content);

        // 네이티브 광고 노출
        nativeAdItem.attach(view, binder);
    }
}
```

### 차이점

1) Placement ID를 광고 로드 시점이 아닌 **NativeAdItem 생성 시점**에 입력하도록 변경되었습니다.

2) 광고 정보를 뷰에 맵핑을 하기 위해서 **네이티브 바인더** 기능이 추가 되었으며 각 뷰의 ID를 넣어주면 SDK에서 광고 데이터를 넣어줍니다.

## 5. 동영상 광고 (Video Ad)

### 구 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	...
    
    TnkSession.prepareVideoAd(this, "Logic ID", new VideoAdListener() {

        @Override
        public void onClose(int type) {
        }

        @Override
        public void onFailure(int errCode) {
            Log.d("Test", "errCode : " + errCode);
        }

        @Override
        public void onLoad() {
          	// 비디오 광고 로드 완료 후 showVideoAd
            TnkSession.showVideoAd(GuideTestActivity.this, "Logic ID");
        }

        @Override
        public void onShow() {
        }

        @Override
        public void onVideoCompleted(boolean skipped) {
            Log.d("Test", "video onVideoCompleted");
        }
    }, true);
  
  ...
}
```

### 신규 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...
    
    final InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this, "Placement ID");
    interstitialAdItem.setListener(new AdListener() {
        @Override
        public void onClose(AdItem adItem, int type) {
            // 종료 버튼 선택 시 앱을 종료합니다.
            if (type == AdListener.CLOSE_EXIT) {
                finish();
            }
        }

        @Override
        public void onError(AdItem adItem, AdError error) {
            Log.e("Test", "Error : " + error.getMessage());
        }

        @Override
        public void onLoad(AdItem adItem) {
            // 광고가 로드 완료된 후 show를 호출해주어야 합니다.
            interstitialAdItem.show();
        }

        @Override
        public void onShow(AdItem adItem) {
        }

        @Override
        public void onClick(AdItem adItem) {
        }
      
      	@Override
        public void onVideoCompletion(AdItem adItem, int verifyCode) {
            super.onVideoCompletion(adItem, verifyCode);

            if (verifyCode >= VIDEO_VERIFY_SUCCESS_SELF) {
                // 적립 성공
            } else {
                // 적립 실패
            }
        }
    });

    interstitialAdItem.load();
  
  ...
}
```

### 차이점

신규 SDK 동영상 광고는 전면 광고 사용방법과 동일하며 Placement ID 생성 후 광고 설정을 동영상으로 설정해주시면 동영상 광고 사용이 가능합니다.