# Migration Guide (for Android)

구 SDK를 사용 중이던 매체사에서는 해당 마이그레이션 가이드를 참고하면 쉽게 신규 SDK를 적용할 수 있습니다.

## 1. 공통 변경 사항

1) **App ID**의 명칭을 **Inventory ID**로 변경하였습니다.

2) **광고 로직 ID**의 명칭을 **Placement ID**로 변경하였습니다.

3) 신규 SDK에서 모든 광고 리스너는 **AdListener**로 통합되어 사용됩니다.

4) 광고 리스너의 onFailure에서 제공되는 **AdError 클래스**를 사용하여 에러의 원인 파악이 쉬워졌습니다.



## 2. 전면 광고 (Interstitial Ad) 마이그레이션

구 SDK에서 신규 SDK로 전면 광고를 마이그레이션 하기 위해 사용 방법을 비교해보면 아래와 같습니다.

#### 구 SDK 사용 방법

```java
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
```

#### 신규 SDK 사용 방법

```java
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
```

#### 차이점

1) **InterstitialAdItem** 클래스가 추가되어 광고를 로드하고 노출할 수 있게 변경되었습니다.

2) 광고 리스너에 광고 클릭을 감지할 수 있는 **onClick** 추가되었습니다.

3) **show** 호출 시점을 광고 로드 완료 후로 바꾸었습니다.



## 3. 배너광고 (Banner Ad) 마이그레이션

배너 광고를 사용하는 방법은 Xml 방식과 뷰 동적 생성 방식 두 가지가 있습니다.

구 SDK에서 신규 SDK로 전면 광고를 마이그레이션 하기 위해 사용 방법을 비교해보면 아래와 같습니다.

### XML 방식

첫 번째로 XML 방식을 비교해 봅시다.

#### 구 SDK 사용 방법

##### XML 뷰 삽입 방식

```xml
<com.tnkfactory.ad.BannerAdView
    android:id="@+id/banner_ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

##### 배너 광고 로드

```java
BannerAdView bannerAdView = findViewById(R.id.banner_ad_view);
bannerAdView.setBannerAdListener(new BannerAdListener() {
    @Override
    public void onFailure(int errCode) {
        Log.d("Test", "errCode : " + errCode);
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onClick() {
    }
});

bannerAdView.loadAd("Display Logic");
```

생명주기 관리

```

```



#### 신규 SDK 사용 방법

##### XML 뷰 삽입 방식

```xml
<com.tnkfactory.ad.BannerAdView
		android:id="@+id/banner_ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:placement_id="Placement_ID" />
```

##### 배너 광고 로드

```java
BannerAdView bannerAdView = findViewById(R.id.banner_ad_view);
bannerAdView.setListener(new AdListener() {
    @Override
    public void onError(AdItem adItem, AdError error) {
        Log.d("Test", "errCode : " + error.getMessage());
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
```

#### 차이점

1) XML에 BannerAdView 옵션으로 **placement_id**가 추가되어 광고 로드 시점이 아닌 뷰 삽입 시점에 입력하도록 변경되었습니다.

2) 광고 리스너에 광고 로드 완료를 감지할 수 있는 **onLoad** 추가되었습니다.

---------------------------------여기까지 작성함(기존 SDK 배너 생명주기 관리 추가 작성 필요)



### 뷰 동적 생성 방식

Placement ID를 입력하여 BannerAdView를 생성 후 배너 광고를 로드해줍니다.

```java
BannerAdView bannerAdView = new BannerAdView(this, "YOUR-PLACEMENT-ID");
bannerAdView.load();
```



## 4. 피드형 광고 (Feed Ad)

피드형 광고를 사용하는 방법은 Xml 방식과 뷰 동적 생성 방식 두 가지가 있습니다.

> Xml 뷰 삽입 방식

레이아웃 Xml 내에 아래와 같이 FeedAdView를 넣어줍니다.

이때 Placement ID를 입력해줍니다.

```xml
<com.tnkfactory.ad.FeedAdView
		android:id="@+id/feed_ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:placement_id="YOUR-PLACEMENT-ID"/>
```

```
FeedAdView feedAdView = findViewById(R.id.feed_ad_view);
feedAdView.load();
```

> 뷰 동적 생성 방식

코드로 Placement ID를 사용하여 FeedAdView를 생성 후 피드형 광고를 로드해줍니다.

```
FeedAdView feedAdView = new FeedAdView(this, "YOUR-PLACEMENT-ID");
```



## 5. 네이티브 광고 (Native Ad)

> 레이아웃 생성 (native_ad_item.xml)

네이티브 광고를 보여줄 레이아웃을 생성합니다.

```xml
<RelativeLayout
	xmlns:android="http://schemas.android.com/apk/res/android"
	xmlns:tools="http://schemas.android.com/tools"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	android:padding="6dp"
	android:background="#FFFFFF">

	<RelativeLayout
		android:id="@+id/native_ad_image_layout"
		android:layout_width="match_parent"
		android:layout_height="wrap_content">

		<FrameLayout
			android:id="@+id/native_ad_content"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"
			tools:background="#FF5722"
			tools:layout_height="300dp" />
		
		<ImageView
			android:id="@+id/native_ad_watermark_container"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_alignParentRight="true"
			tools:background="#FFEB3B" />
	</RelativeLayout>

	<TextView
		android:id="@+id/native_ad_rating"
		android:layout_width="100dp"
		android:layout_height="20dp"
		android:layout_centerHorizontal="true"
		android:layout_below="@id/native_ad_image_layout"
		android:layout_marginTop="5dp"
		android:layout_marginLeft="5dp"
		android:textColor="#ffffffff"
		android:textSize="16dp"/>

	<RelativeLayout
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:layout_marginTop="5dp"
		android:layout_below="@+id/native_ad_rating">

		<ImageView
			android:id="@+id/native_ad_icon"
			android:layout_width="72dp"
			android:layout_height="72dp"
			android:layout_alignParentTop="true"
			android:layout_alignParentLeft="true"
			android:padding="4dp"
			android:scaleType="fitXY"
			tools:background="#00BCD4" />
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
			android:textSize="17sp"
			tools:background="#03A9F4" />
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
			android:textSize="13sp"
			tools:background="#2196F3" />
	</RelativeLayout>
</RelativeLayout>
```

> 네이티브 광고 로드

```java
NativeAdItem nativeAdItem = new NativeAdItem(this, "YOUR-PlACEMENT-ID");
nativeAdItem.load();
```

> 네이티브 광고 노출

로드 완료 후 진행합니다.

```java
if (nativeAdItem != null & nativeAdItem.isLoaded()) {

    // 네이티브 광고가 삽입될 컨테이너 초기화
    ViewGroup adContainer = findViewById(R.id.native_ad_container);
    adContainer.removeAllViews();

    // 네이티브 아이템 레이아웃 삽입
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    RelativeLayout adItemView = (RelativeLayout) inflater.inflate(R.layout.native_ad_item, null);
    adContainer.addView(adItemView);

    // 네이티브 바인더 셋팅
    NativeViewBinder binder = new NativeViewBinder(R.id.native_ad_content);
    binder.iconId(R.id.native_ad_icon)
            .titleId(R.id.native_ad_title)
            .textId(R.id.native_ad_desc)
            .starRatingId(R.id.native_ad_rating)
            .watermarkIconId(R.id.native_ad_watermark_container)
            .addClickView(R.id.native_ad_content);

    // 광고 노출
    nativeAdItem.attach(adContainer, binder);
}
```



## 6. 동영상 광고 (Video Ad)

동영상 광고는 전면 광고와 사용 방법이 같습니다.

> 전면 광고 로드

```java
InterstitialAdItem interstitialAdItem = new InterstitialAdItem(this, "YOUR-PlACEMENT-ID");
interstitialAdItem.load();
```

> 전면 광고 노출

로드 완료 후 진행합니다.

```java
if (interstitialAdItem.isLoaded()) {
		interstitialAdItem.show();
}
```

> 리워드 동영상 광고 적립 여부 확인

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
     * 화면 닫힐 때 호출됨 (배너는 다른 광고가 로딩될때 이전 광고에 대하여 호출됨, native 는 detach 시점에 호출됨)
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



## 7. 미디에이션 (Mediation)

[[AdMob 미디에이션 설정]](https://support.google.com/admob/answer/3124703?hl=ko) 을 선행 후 [[맞춤이벤트 어댑터]](./google_mediation/adapter)을 다운로드 받으셔서 개발중인 앱 프로젝트에 필요한 맞춤이벤트 어댑터를 복사하여 넣으신 후 해당 클래스의 placementId 변수에 발급받으신 ID를 넣어주시기 바랍니다.



###### [맞춤 이벤트 추가 예시]

AdMob 로그인 후 메뉴에서 미디에이션 탭을 누르시면 아래 이미지와 같이 미디에이션 그룹을 만들 수 있는 화면이 표시됩니다.

[미디에이션 그룹 만들기] 버튼을 클릭하셔서 그룹을 생성해 주세요. 

![mediation_guide_01](./google_mediation/img/mediation_guide_01.png)



미디에이션 그룹 생성 시 맞춤 이벤트를 추가합니다.

![mediation_guide_02](./google_mediation/img/mediation_guide_02.png)



맞춤 이벤트 추가 시 Class Name 항목에 개발중인 앱 프로젝트에 복사해 넣은 맞춤이벤트 어댑터의 실제 결로를 입력합니다.

![mediation_guide_03](./google_mediation/img/mediation_guide_03.png)
