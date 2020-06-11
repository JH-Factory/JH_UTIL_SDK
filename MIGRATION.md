# Migration Guide (for Android)

구 SDK를 사용 중이던 매체사에서는 해당 마이그레이션 가이드를 참고하면 쉽게 신규 SDK를 적용할 수 있습니다.

## 목차

1. [필수 사항](#1-필수-사항)
   * [사이트 Migration](#사이트-migration)
   * [프로젝트에서 구 SDK 제거](#프로젝트에서-구-sdk-제거)
   * [신규 Publisher SDK 설정하기](#신규-publisher-sdk-설정하기)
   * [Tnk Publisher SDK 가이드](#tnk-publisher-sdk-가이드)
2. [공통 변경 사항](#2-공통-변경-사항)
3. [전면 광고 (Interstitial Ad) 마이그레이션](#3-전면-광고-interstitial-ad-마이그레이션)
   * [구 SDK 사용 방법](#구-sdk-사용-방법)
   * [신규 SDK 사용 방법](#신규-sdk-사용-방법)
   * [차이점](#차이점)
4. [배너 광고 (Banner Ad) 마이그레이션](#4-배너-광고-banner-ad-마이그레이션)
   * [XML 뷰 삽입 방식](#xml-뷰-삽입-방식)
     * [구 SDK 사용 방법](#구-sdk-사용-방법-1)
     * [신규 SDK 사용 방법](#신규-sdk-사용-방법-1)
     * [차이점](#차이점-1)
   * [뷰 동적 생성 방식](#뷰-동적-생성-방식)
     * [구 SDK 사용 방법](#구-sdk-사용-방법-2)
     * [신규 SDK 사용 방법](#신규-sdk-사용-방법-2)
     * [차이점](#차이점-2)
5. [네이티브 광고 (Native Ad) 마이그레이션](#5-네이티브-광고-native-ad-마이그레이션)
   * [구 SDK 사용 방법](#구-sdk-사용-방법-3)
   * [신규 SDK 사용 방법](#신규-sdk-사용-방법-3)
   * [차이점](#차이점-3)
6. [동영상 광고 (Video Ad)](#6-동영상-광고-video-ad)
   * [구 SDK 사용 방법](#구-sdk-사용-방법-4)
   * [신규 SDK 사용 방법](#신규-sdk-사용-방법-4)
   * [차이점](#차이점-4)

## 1. 필수 사항

### 사이트 Migration

신규 Publisher SDK를 위하여 새로운 Publisher Site가 오픈되었습니다. 그러므로 우선 Publisher Site에 계정 생성 및 인벤토리 생성 작업이 필요합니다.
다만 개발자의 편의를 위하여 저희 Tnk의 영업담당자를 통하여 Migration 을 요청하시면 기존 App 및 로직 ID 설정을 새로운 사이트에 맞추어 이관해드리고 있습니다. 그러므로 우선 Site 이관 요청을 해주시고 아래의 SDK Migration 작업을 수행해주시기 바랍니다.

### 프로젝트에서 구 SDK 제거

신규 Publisher SDK를 사용하기 위해서는 기존에 사용하던 구 SDK의 제거 후 사용이 가능합니다.

삽입한 [tnkad_sdk.aar] 파일을 삭제하고 해당 파일을 프로젝트에 참조하던 설정을 제거해주세요.

##### 삭제 예시

구 SDK 가이드의 라이브러리 등록 방식(File > New Module)으로 SDK 참조 했을 경우 예시입니다.

###### gradle에서 아래 부분 삭제

```
dependencies {
    implementation project(":tnkad_sdk") // 이 부분 삭제
}
```

###### SDK 파일 삭제

![migration_guide_01](./img/migration_guide_01.png)

### 신규 Publisher SDK 설정하기

아래의 코드를 App Module의 build.gradle 파일에 추가해주세요.

[![Download](https://api.bintray.com/packages/tnkfactory/android-sdk/pub/images/download.svg)](https://bintray.com/tnkfactory/android-sdk/pub/_latestVersion)

```gradle
dependencies {
    implementation 'com.tnkfactory.ad:pub:7.05.2'
}
```

AndroidManifest.xml 파일에 아래의 권한을 추가해주세요.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Proguard 를 사용하시는 경우 Proguard 설정 파일에 아래의 내용을 반드시 넣어주세요.

```proguard
 -keep class com.tnkfactory.** { *;}
```

### Tnk Publisher SDK 가이드

더 상세한 Tnk Publisher SDK 사용방법은 [Tnk Publisher SDK (for Android)](./README.md)를 참조하시면 됩니다. 

## 2. 공통 변경 사항

1) **App ID**의 명칭을 **Inventory ID**로 변경하였습니다.

2) **광고 로직 ID**의 명칭을 **Placement ID**로 변경하였습니다.

* 광고를 노출하기 위해서는 광고별 Placement ID 생성 및 광고 설정은 필수입니다. (Site 이관을 요청하셨다면 기존에 사용하는 App ID, 광고로직 ID를 그대로 이용하실 수 있습니다.)
* 구 SDK에서 광고 로직 ID 설정없이 TnkSession.CPC를 넣어 사용하던 광고의 경우 Placement ID를 넣는 란에 아래를 참고하시어 해당 명칭을 넣어주시면 됩니다.

  + 전면광고 : DEFAULT_INTERSTITIAL
  + 배너광고 : DEFAULT_BANNER
  + 네이티브광고 : DEFAULT_NATIVE
  + 동영상광고 : DEFAULT_VIDEO

3) 구 SDK에서는 각 광고 타입별 리스너가 구분되어 존재했으나 신규 SDK에서 모든 광고 리스너는 **AdListener** 하나로 통합되어 사용됩니다.

4) AdListener의 onFailure에서 제공되는 **AdError 클래스**의 getMessage()를 사용하여 에러의 원인 파악이 쉬워졌습니다.

## 3. 전면 광고 (Interstitial Ad) 마이그레이션

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
          // 광고 로드 완료 후 노출
          TnkSession.showInterstitialAd(this);
        }

        @Override
        public void onShow() {
        }
    });    
  
  ...
}
```

### 신규 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...
    
    InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this, "Placement ID");
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
            // 광로 로드 완료 후 노출
            adItem.show();
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

1) 전면 광고를 사용방법은 구 SDK에서는 TnkSession 클래스를 통해 가능했으나 신규 SDK에서는 **InterstitialAdItem** 클래스가 추가되어 전면 광고를 사용할 수 있도록 변경되었습니다.

2) 광고 리스너는 AdListener 클래스를 사용합니다. 또한 AdListener 의 메소드들은 필요한 메소드만 구현하시고 사용하지 않는 메소드는 삭제하시면 됩니다.

3) 전면 광고 클릭 감지의 경우 구 SDK에서는 TnkAdListener의 onClose() 매개변수 type을 통해 감지할 수 있었으나 신규 SDK는 **onClick**을 분리하는 것으로 변경되었습니다.



## 4. 배너 광고 (Banner Ad) 마이그레이션

배너 광고를 사용하는 방법은 XML 뷰 삽입 방식과 뷰 동적 생성 방식 두 가지가 있습니다.

구 SDK에서 신규 SDK로 전면 광고를 마이그레이션 하기 위해 사용 방법을 비교해보면 아래와 같습니다.

### XML 뷰 삽입 방식

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
  
    bannerAdView.load();
  
  ...
}
```

#### 차이점

1) XML에 BannerAdView 옵션으로 **placement_id**가 추가되어 광고 로드 시점이 아닌 뷰 삽입 시점에 Placement ID를 입력하도록 변경되었습니다.

2) 생명주기 관리 함수가 삭제되고 **자동처리** 해주도록 변경되어 구 SDK에서 사용중이던 onPause, onResume, onDestroy 로직들은 삭제하시면 됩니다.

### 뷰 동적 생성 방식

#### 구 SDK 사용 방법

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...
    
    RelativeLayout mainLayout = findViewById(R.id.main_layout);
    BannerAdView bannerAdView = new BannerAdView(this);
    mainLayout.addView(bannerAdView);

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

    // 배너 광고 로드
    bannerAdView.load();
  
  ...
}
```

#### 차이점

1) Placement ID를 광고 로드 시점이 아닌 **BannerAdView 생성 시점**에 입력하도록 변경되었습니다.

2) 생명주기 관리 함수가 삭제되고 **자동처리** 해주도록 변경되어 구 SDK에서 사용중이던 onPause, onResume, onDestroy 로직들은 삭제하시면 됩니다.

## 5. 네이티브 광고 (Native Ad) 마이그레이션

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
          	// 네이티브 광고 노출
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

2) 광고 정보를 뷰에 맵핑을 하기 위해서 **네이티브 바인더** 기능이 추가 되었으며 바인더에 필요한 각 뷰의 ID를 넣어주면 SDK에서 광고 데이터를 삽입하여 노출합니다. 이때 광고의 메인 컨텐츠(이미지) 뷰의 ID 는 네이티브 바인더 생성시 필수로 입력해주어야 합니다.

## 6. 동영상 광고 (Video Ad)

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
            adItem.show();
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

          	// 적립 성공 여부 확인
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

1) 동영상 광고의 사용방법은 구 SDK에서는 TnkSession 클래스를 통해 가능했으나 신규 SDK에서는 **InterstitialAdItem** 클래스가 추가되어 동영상 광고를 사용할 수 있도록 변경되었습니다.

2) 신규 SDK의 동영상 광고는 전면 광고 사용방법과 동일하며 Placement ID 생성 후 광고 설정을 동영상으로 설정해주시면 동영상 광고가 노출됩니다.

3) 구 SDK에서는 VideoAdListener를 통해 비디오 광고의 재생완료 여부만 확인할 수 있었으나 신규 SDK에서는 AdListener의 onVideoCompletion() 매개변수 verifyCode를 통해서 동영상 시청을 통한 리워드가 지급 되었는지 여부를 확인하는 기능이 추가되었습니다.

