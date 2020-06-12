# Tnk Publisher SDK (for Android)

## 목차

1. [SDK 설정하기](#1-sdk-설정하기)
   * [Test Flight](#test-flight)
   * [Publisher ID 등록하기](#publisher-id-등록하기)
   * [COPPA 설정](#coppa-설정)
2. [전면 광고 (Interstitial Ad)](#2-전면-광고-interstitial-ad)
   * [전면 광고 객체 생성](#전면-광고-객체-생성)
   * [전면 광고 띄우기](#전면-광고-띄우기)
     * [광고 로드 후 바로 노출](#광고-로드-후-바로-노출)
     * [광고 로드 후 일정시간 후에 노출](#광고-로드-후-일정시간-후에-노출)
   * [종료 시 전면 광고 사용 방법](#종료-시-전면-광고-사용-방법)
3. [배너 광고 (Banner Ad)](#3-배너-광고-banner-ad)
   * [XML 뷰 삽입 방식](#xml-뷰-삽입-방식)
     * [배너 뷰 생성](#배너-뷰-생성)
     * [배너 광고 로드](#배너-광고-로드)
   * [뷰 동적 생성 방식](#뷰-동적-생성-방식)
     * [부모 레이아웃 생성](#부모-레이아웃-생성)
     * [배너 광고 로드](#배너-광고-로드-1)
4. [피드형 광고 (Feed Ad)](#4-피드형-광고-feed-ad)
   * [XML 뷰 삽입 방식](#xml-뷰-삽입-방식-1)
     * [피드 뷰 생성](#피드-뷰-생성)
     * [피드 광고 로드](#피드-광고-로드)
   * [뷰 동적 생성 방식](#뷰-동적-생성-방식-1)
     * [부모 레이아웃 생성](#부모-레이아웃-생성-1)
     * [피드 광고 로드](#피드-광고-로드-1)
5. [네이티브 광고 (Native Ad)](#5-네이티브-광고-native-ad)
   * [레이아웃 생성](#레이아웃-생성)
   * [네이티브 객체 생성](#네이티브-객체-생성)
   * [네이티브 광고 띄우기](#네이티브-광고-띄우기)
     * [광고 로드 후 바로 노출](#광고-로드-후-바로-노출-1)
     * [광고 로드 후 일정시간 후에 노출](#광고-로드-후-일정시간-후에-노출-1)
6. [동영상 광고 (Video Ad)](#6-동영상-광고-video-ad)
   * [리워드 동영상 광고 적립 여부 확인](#리워드-동영상-광고-적립-여부-확인)
7. [AdListener 사용 방법](#7-adlistener-사용-방법)
8. [미디에이션 (Mediation)](#8-미디에이션-mediation)
   * [맞춤 이벤트 추가 예시](#맞춤-이벤트-추가-예시)

## 1. SDK 설정하기

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

### Test Flight

아래의 코드를 사용하어 간단하게 테스트 광고를 띄워보세요.
> SDK import

```java
import com.tnkfactory.ad.*;
```

> 전면 광고 (Interstitial Ad)

```java
InterstitialAdItem adItem = new InterstitialAdItem(this,"TEST_INTERSTITIAL_V", new AdListener() {
        @Override
        public void onLoad(AdItem adItem) {
            adItem.show();
        }
    });

adItem.load(); 
```
> 배너 광고 (Banner Ad)

```xml
<com.tnkfactory.ad.BannerAdView
    android:id="@+id/banner_ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true"
    android:background="#ffffffff"
    app:placement_id="TEST_BANNER_100"/>
```

```java
BannerAdView bannerAdView = findViewById(R.id.banner_ad_view);
bannerAdView.load();
```
> Test Flight 용 Placement 들

아래와 같이 광고 유형별로 Test Flight 용 Placement 들을 제공하고 있습니다. 아래의 Placement ID 를 사용하시면 별도로 계정이나 앱을 등록하지 않아도 간단하게 테스트 광고를 띄워보실 수 있습니다.

- TEST_BANNER_100 : 배너 광고 (640x100)
- TEST_BANNER_200 : 배너 광고 (640x200)
- TEST_INTERSTITIAL_H : 전면 광고 가로
- TEST_INTERSTITIAL_V : 전면 광고 세로
- TEST_INTERSTITIAL_V_FINISH : 전면 광고 세로 (종료시 2-Button 형)
- TEST_FEED : 피드형 광고
- TEST_NATIVE : 네이티브 광고
- TEST_REWARD_V : 리워드 동영상 광고

### Publisher ID 등록하기

Test Flight 에서는 별도로 계정등록을 하지않아도 간단히 테스트를 진행할 수 있었습니다. 하지만 실제 광고를 받기 위해서는 우선 Tnk Publish Site 에서 Inventory를 등록하여 발급받은 ID 를 AndroidManifest.xml 파일에 추가하셔야합니다.
아래의 샘플을 참고하시어 실제 ID 를 등록하세요.

```xml
<application
   ...
   >
      ...

        <meta-data android:name="tnk_pub_id" android:value="YOUR-INVENTORY-ID-HERE" />

      ...
</application>
```

실제 ID 를 등록하면 위 Test Flight 코드에서는 더 이상 광고가 나타나지 않습니다. Tnk Publish Site 에서 광고 유형에 맞추어 Placement 를 등록하시고 등록한 Placement의 이름을 사용하셔야 실제 광고가 표시됩니다.

### COPPA 설정

COPPA는 [미국 어린이 온라인 개인정보 보호법](https://www.ftc.gov/tips-advice/business-center/privacy-and-security/children's-privacy) 및 관련 법규입니다. 구글 에서는 앱이 13세 미만의 아동을 대상으로 서비스한다면 관련 법률을 준수하도록 하고 있습니다. 연령에 맞는 광고가 보일 수 있도록 아래의 옵션을 설정하시기 바랍니다.

```java
AdConfiguration.setCOPPA(this, true); // ON - 13세 미안 아동을 대상으로 한 서비스 일경우 사용
AdConfiguration.setCOPPA(this, false); // OFF - 기본값
```



## 2. 전면 광고 (Interstitial Ad)

### 전면 광고 객체 생성

SDK 클래스들을 import 해주세요.
```java
import com.tnkfactory.ad.*;
```

아래와 같이 Placement ID를 입력하여 전면 광고 객체를 생성합니다.
```java
@Override
public void onCreate(Bundle savedInstanceState) {
  ...

    InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this, "YOUR-PlACEMENT-ID");
    
  ...
}
```

### 전면 광고 띄우기

#### 광고 로드 후 바로 노출

전면 광고가 로드되는 시점에 바로 광고를 띄우려면 AdListener 를 사용합니다.

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      ...

        // 전면 광고 객체 생성
        InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this,"YOUR-PlACEMENT-ID");
      	// AdListener를 사용해 전면 광고가 로드되는 시점에 노출
        interstitialAdItem.setLisenter(new AdListener() {
            @Override
            public void onLoad(AdItem adItem) {
                adItem.show();
            }
        });

      	// 전면 광고 로드
        interstitialAdItem.load();
        
      ...
    }
}
```

#### 광고 로드 후 일정시간 후에 노출

만약 광고를 로드하고 일정시간 후에 광고를 띄우시려면 아래와 같이 광고가 성공적으로 로딩되었는지 확인한 후 광고를 띄우실 수 있습니다.

```java
// 전면 광고 객체 생성
InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this,"YOUR-PlACEMENT-ID");
// 전면 광고 로드
interstitialAdItem.load();

...

// 일정시간 후에 광고가 로드 되었는지 확인 후 show 호출 
// load와 show를 동시 호출하면 광고 미로드 상태로 전면 광고가 노출되지 않습니다.
if (interstitialAdItem.isLoaded()) {
    interstitialAdItem.show();
}
```

### 종료 시 전면 광고 사용 방법

'2-Button' 프레임을 사용하여 앱 종료 시 전면 팝업 광고를 자연스럽게 삽입 가능합니다.

우선 Publisher Site 에서 해당 Placement의 프레임을 2-Button 프레임으로 설정하시고, 앱에서 종료버튼 클릭시 처리내용을 AdListener의 onClose() 에서 아래의 내용을 참고하여 구현하시면 됩니다.

```java
interstitialAdItem.setListener(new AdListener() {
  ...

    /**
     * 화면 닫힐 때 호출됨
     * @param adItem 이벤트 대상이되는 AdItem 객체
     * @param type 0:simple close, 1: auto close, 2:exit
     */
    @Override
    public void onClose(AdItem adItem, int type) {
        // 종료 버튼 선택 시 앱을 종료합니다.
        if (type == AdListener.CLOSE_EXIT) {
            MainActivity.this.finish();
        }
    }

  ...
});
```

## 3. 배너 광고 (Banner Ad)

배너 광고를 사용하는 방법은 XML 뷰 삽입 방식과 뷰 동적 생성 방식 두 가지가 있습니다.

### XML 뷰 삽입 방식

#### 배너 뷰 생성

레이아웃 XML 내에 아래와 같이 배너 뷰를 생성합니다.

이때 Placement ID를 입력해줍니다.

```xml
<RelativeLayout
   ...
   >
      ...
  
        <com.tnkfactory.ad.BannerAdView
            android:id="@+id/banner_ad_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:placement_id="YOUR-PLACEMENT-ID" />
  
      ...
</RelativeLayout>
```

#### 배너 광고 로드

SDK 클래스들을 import 해주세요.
```java
import com.tnkfactory.ad.*;
```

XML에 삽입된 배너 뷰에 아래와 같이 광고를 로드합니다.

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	...
        
        BannerAdView bannerAdView = findViewById(R.id.banner_ad_view);
    
    		// 배너 광고 로드
        bannerAdView.load();
    
    	...
    }
}
```

### 뷰 동적 생성 방식

#### 부모 레이아웃 생성

레이아웃 XML 내에 아래와 같이 배너 뷰를 넣어 줄 부모 레이아웃을 생성합니다.

```xml
<RelativeLayout
   ...
   >
      ...
  
        <RelativeLayout
            android:id="@+id/banner_ad_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
  
      ...
</RelativeLayout>
```

#### 배너 광고 로드

SDK 클래스들을 import 해주세요.
```java
import com.tnkfactory.ad.*;
```

아래와 같이 Placement ID를 입력하여 배너 뷰 생성 후 배너 광고를 로드해줍니다.

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      ...
        
        RelativeLayout bannerAdLayout = findViewById(R.id.banner_ad_layout);
      
      	// 배너 뷰 객체 생성
        BannerAdView bannerAdView = new BannerAdView(this, "YOUR-PLACEMENT-ID");
      
      	// 부모 레이아웃에 배너 뷰 삽입
        bannerAdLayout.addView(bannerAdView);

      	// 배너 광고 로드
        bannerAdView.load();
      
      ...
    }
}
```

## 4. 피드형 광고 (Feed Ad)

피드형 광고를 사용하는 방법은 Xml 방식과 뷰 동적 생성 방식 두 가지가 있습니다.

### Xml 뷰 삽입 방식

#### 피드 뷰 생성

레이아웃 Xml 내에 아래와 같이 피드 뷰를 넣어줍니다.

이때 Placement ID를 입력해줍니다.

```xml
<RelativeLayout
   ...
   >
      ...
  
        <com.tnkfactory.ad.FeedAdView
            android:id="@+id/feed_ad_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:placement_id="YOUR-PLACEMENT-ID"/>
  
      ...
</RelativeLayout>
```

#### 피드 광고 로드

SDK 클래스들을 import 해주세요.
```java
import com.tnkfactory.ad.*;
```

XML에 삽입된 피드 뷰에 아래와 같이 광고를 로드합니다.

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FeedAdView feedAdView = findViewById(R.id.feed_ad_view);

        // 피드 광고 로드
        feedAdView.load();
    }
}
```

### 뷰 동적 생성 방식

#### 부모 레이아웃 생성

레이아웃 XML 내에 아래와 같이 피드 뷰를 넣어 줄 부모 레이아웃을 생성합니다.

```xml
<RelativeLayout
   ...
   >
      ...
  
        <RelativeLayout
            android:id="@+id/feed_ad_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
  
      ...
</RelativeLayout>
```

#### 피드 광고 로드

SDK 클래스들을 import 해주세요.
```java
import com.tnkfactory.ad.*;
```

아래와 같이 Placement ID를 입력하여 피드 뷰 생성 후 배너 광고를 로드해줍니다.

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      ...
        
        RelativeLayout feedAdLayout = findViewById(R.id.feed_ad_layout);

        // 피드 뷰 객체 생성
        FeedAdView feedAdView = new FeedAdView(this, "YOUR-PLACEMENT-ID");

        // 부모 레이아웃에 피드 뷰 삽입
        feedAdLayout.addView(feedAdView);

        // 피드 광고 로드
        feedAdView.load();
      
      ...
    }
}
```



## 5. 네이티브 광고 (Native Ad)

### 레이아웃 생성 

네이티브 광고를 보여줄 레이아웃(native_ad_item.xml)을 생성합니다.

아래 레이아웃은 예시이며 실제로 사용시 원하시는 구조로 만드시면 됩니다.

```xml
<RelativeLayout
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	android:padding="6dp"
	android:background="#DDDDDD">

	<RelativeLayout
		android:id="@+id/native_ad_image_layout"
		android:layout_width="match_parent"
		android:layout_height="wrap_content">

		<FrameLayout
			android:id="@+id/native_ad_content"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"/>

		<ImageView
			android:id="@+id/native_ad_watermark_container"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_alignParentRight="true"/>
	</RelativeLayout>

	<RelativeLayout
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:layout_marginTop="10dp"
		android:layout_below="@+id/native_ad_image_layout">

		<ImageView
			android:id="@+id/native_ad_icon"
			android:layout_width="72dp"
			android:layout_height="72dp"
			android:layout_alignParentTop="true"
			android:layout_alignParentLeft="true"
			android:padding="4dp"
			android:scaleType="fitXY"/>
		<TextView
			android:id="@+id/native_ad_title"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"
			android:layout_toRightOf="@id/native_ad_icon"
			android:layout_alignParentTop="true"
			android:layout_marginTop="3dp"
			android:layout_marginLeft="8dp"
			android:gravity="center_vertical"
			android:textColor="#ff020202"
			android:textSize="17sp"/>
		<TextView
			android:id="@+id/native_ad_desc"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"
			android:layout_toRightOf="@id/native_ad_icon"
			android:layout_below="@id/native_ad_title"
			android:layout_marginLeft="8dp"
			android:layout_marginTop="8dp"
			android:gravity="center_vertical"
			android:textColor="#ff179dce"
			android:textSize="13sp"/>
	</RelativeLayout>
</RelativeLayout>
```

### 네이티브 객체 생성

SDK 클래스들을 import 해주세요.
```java
import com.tnkfactory.ad.*;
```

```java
@Override
public void onCreate(Bundle savedInstanceState) {
  ...

    NativeAdItem nativeAdItem = new NativeAdItem(this, "YOUR-PlACEMENT-ID");
    
  ...
}
```

### 네이티브 광고 띄우기

#### 광고 로드 후 바로 노출

네이티브 광고가 로드되는 시점에 바로 광고를 띄우려면 AdListener 를 사용합니다.

```java
public class MainActivity extends AppCompatActivity {
  ...
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      ...

        NativeAdItem nativeAdItem = new NativeAdItem(this, "YOUR-PlACEMENT-ID");

        // AdListener를 사용해 네이티브 광고가 로드되는 시점에 노출
        nativeAdItem.setListener(new AdListener() {

            @Override
            public void onLoad(AdItem adItem) {
                // 네이티브 광고 노출
                showNativeAd((NativeAdItem) adItem);
            }
        });

        // 네이티브 광고 로드
        nativeAdItem.load();
      
      ...
    }

  ...
    
    // 네이티브 광고 노출
    private void showNativeAd(NativeAdItem nativeAdItem) {

        if (nativeAdItem != null & nativeAdItem.isLoaded()) {

            // 네이티브 광고가 삽입될 컨테이너 초기화
            ViewGroup adContainer = findViewById(R.id.native_ad_container);
            adContainer.removeAllViews();

            // 컨테이너에 네이티브 아이템 레이아웃 삽입
            ViewGroup view = (ViewGroup) View.inflate(this, R.layout.native_ad_item, adContainer);

            // 네이티브 바인더 객체 생성
            // 생성자에 메인 컨텐츠가 표시될 뷰 ID 필수 입력
            NativeViewBinder binder = new NativeViewBinder(R.id.native_ad_content);

            // 네이티브 바인더 셋팅
            binder.iconId(R.id.native_ad_icon)
                    .titleId(R.id.native_ad_title)
                    .textId(R.id.native_ad_desc)
                    .watermarkIconId(R.id.native_ad_watermark_container)
                    .addClickView(R.id.native_ad_content);

            // 네이티브 광고 노출
            nativeAdItem.attach(view, binder);
        }
    }
  
  ...
}
```

#### 광고 로드 후 일정시간 후에 노출

만약 광고를 로드하고 일정시간 후에 광고를 띄우시려면 아래와 같이 광고가 성공적으로 로딩되었는지 확인한 후 광고를 띄우실 수 있습니다.

```java
// 전면 광고 객체 생성
NativeAdItem nativeAdItem = new NativeAdItem(this,"YOUR-PlACEMENT-ID");
// 네이티브 광고 로드
nativeAdItem.load();

...
  
// 일정시간 후에 광고가 로드 되었는지 확인 후 show 호출
// load와 show를 동시 호출하면 광고 미로드 상태로 전면 광고가 노출되지 않습니다.
if (nativeAdItem.isLoaded()) {

    // 네이티브 광고가 삽입될 컨테이너 초기화
    ViewGroup adContainer = findViewById(R.id.native_ad_container);
    adContainer.removeAllViews();

    // 컨테이너에 네이티브 아이템 레이아웃 삽입
 		ViewGroup view = (ViewGroup) View.inflate(this, R.layout.native_ad_item, adContainer);

    // 네이티브 바인더 객체 생성
    // 생성자에 메인 컨텐츠가 표시될 뷰 ID 필수 입력
    NativeViewBinder binder = new NativeViewBinder(R.id.native_ad_content);

    // 네이티브 바인더 셋팅
    binder.iconId(R.id.native_ad_icon)
            .titleId(R.id.native_ad_title)
            .textId(R.id.native_ad_desc)
            .watermarkIconId(R.id.native_ad_watermark_container)
            .addClickView(R.id.native_ad_content);

    // 네이티브 광고 노출
    nativeAdItem.attach(view, binder);
}
```

## 6. 동영상 광고 (Video Ad)

동영상 광고는 전면 광고와 사용 방법이 같아서 Placement ID를 생성할 때 동영상 광고 설정을 진행해주시고 [전면 광고 가이드](#2-전면-광고-interstitial-ad)를 그대로 활용하시면 됩니다.

### 리워드 동영상 광고 적립 여부 확인

리워드 동영상 광고의 경우 재생 완료 후 AdListener를 사용하여 적립 여부를 확인할 수 있습니다.

```java
interstitialAdItem.setListener(new AdListener() {
  ...

    /**
     * 광고의 재생이 완료되었을 경우 호출됩니다.
     * @param adItem 광고 아이템
     * @param verifyCode 적립 여부
     */
    @Override
    public void onVideoCompletion(AdItem adItem, int verifyCode) {
        super.onVideoCompletion(adItem, verifyCode);

        if (verifyCode >= VIDEO_VERIFY_SUCCESS_SELF) {
            // 적립 성공
        } else {
            // 적립 실패
        }
    }

  ...
});
```



## 7. AdListener 사용 방법

전면, 배너, 피드형, 네이티브 등 모든 광고는 setListener()를 통해 AdListener를 등록하여 사용할 수 있습니다.

필요한 메소드만 Override하여 사용하면 됩니다.

```java
public abstract class AdListener {

    public static int CLOSE_SIMPLE = 0; // 클릭하지 않고 그냥 close
    public static int CLOSE_AUTO = 1; // 자동 닫기 시간이 지나서 close
    public static int CLOSE_EXIT = 2; // 전면인 경우 종료 버튼으로 close

    // video completion 확인 코드
    public static int VIDEO_VERIFY_SUCCESS_S2S = 1; // 매체 서버를 통해서 검증됨
    public static int VIDEO_VERIFY_SUCCESS_SELF = 0; // 매체 서버 URL이 설정되지 않아 Tnk 자체 검증
    public static int VIDEO_VERIFY_FAILED_S2S = -1; // 매체 서버를 통해서 지급불가 판단됨
    public static int VIDEO_VERIFY_FAILED_TIMEOUT = -2; // 매체 서버 호출시 타임아웃 발생
    public static int VIDEO_VERIFY_FAILED_NO_DATA = -3; // 광고 송출 및 노출 이력 데이터가 없음
    public static int VIDEO_VERIFY_FAILED_TEST_VIDEO = -4; // 테스트 동영상 광고임
    public static int VIDEO_VERIFY_FAILED_ERROR = -9; // 그외 시스템 에러가 발생

    /**
     * 화면 닫힐 때 호출됨
     * @param adItem 이벤트 대상이되는 AdItem 객체
     * @param type 0:simple close, 1: auto close, 2:exit
     */
    public void onClose(AdItem adItem, int type) {
      
    }

    /**
     * 광고 클릭시 호출됨
     * 광고 화면은 닫히지 않음
     * @param adItem 이벤트 대상이되는 AdItem 객체
     */
    public void onClick(AdItem adItem) {
      
    }

    /**
     * 광고 화면이 화면이 나타나는 시점에 호출된다.
     * @param adItem 이벤트 대상이되는 AdItem 객체
     */
    public void onShow(AdItem adItem) {

    }

    /**
     * 광고 처리중 오류 발생시 호출됨
     * @param adItem 이벤트 대상이되는 AdItem 객체
     * @param error AdError
     */
    public void onError(AdItem adItem, AdError error) {

    }

    /**
     * 광고 load() 후 광고가 도착하면 호출됨
     * @param adItem 이벤트 대상이되는 AdItem 객체
     */
    public void onLoad(AdItem adItem) {

    }

    /**
     * 동영상이 포함되어 있는 경우 동영상을 끝까지 시청하는 시점에 호출된다.
     * @param adItem 이벤트 대상이되는 AdItem 객체
     * @param verifyCode 동영상 시청 완료 콜백 결과.
     */
    public void onVideoCompletion(AdItem adItem, int verifyCode) {
   
    }
}
```



## 8. 미디에이션 (Mediation)

[[AdMob 미디에이션 설정]](https://support.google.com/admob/answer/3124703?hl=ko) 을 선행 후 [[맞춤이벤트 어댑터]](./google_mediation/adapter)을 다운로드 받으셔서 개발중인 앱 프로젝트에 필요한 맞춤이벤트 어댑터를 복사하여 넣으신 후 해당 클래스의 placementId 변수에 발급받으신 ID를 넣어주시기 바랍니다.



### 맞춤 이벤트 추가 예시

AdMob 로그인 후 메뉴에서 미디에이션 탭을 누르시면 아래 이미지와 같이 미디에이션 그룹을 만들 수 있는 화면이 표시됩니다.

[미디에이션 그룹 만들기] 버튼을 클릭하셔서 그룹을 생성해 주세요. 

![mediation_guide_01](./google_mediation/img/mediation_guide_01.png)



미디에이션 그룹 생성 시 맞춤 이벤트를 추가합니다.

![mediation_guide_02](./google_mediation/img/mediation_guide_02.png)



맞춤 이벤트 추가 시 Class Name 항목에 개발중인 앱 프로젝트에 복사해 넣은 맞춤이벤트 어댑터의 실제 결로를 입력합니다.

![mediation_guide_03](./google_mediation/img/mediation_guide_03.png)
