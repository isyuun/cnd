package net.pettip.app.navi.screen.test

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ArrowheadPathOverlay
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.PathOverlay
import net.pettip.app.navi.screen.map.NaverMapComponent
import net.pettip.app.navi.viewmodel.map.MapViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : TestWalkScreen
 * @Date        : 2024-07-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestWalkScreen
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun TestWalkScreen(
    viewModel: MapViewModel = hiltViewModel(),
){

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Transparent)

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.Basic,
                maxZoom = 18.0,
                minZoom = 5.0,
                isBuildingLayerGroupEnabled = false,
                isCadastralLayerGroupEnabled = false,
                isTransitLayerGroupEnabled = false,
                locationTrackingMode = LocationTrackingMode.Follow,
                symbolScale = 1.0f
            )
        )
    }

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isLocationButtonEnabled = true,
                isLogoClickEnabled = false,
            )
        )
    }

    var path by remember{ mutableStateOf(listOf<LatLng>()) }

    var locationList by remember { mutableStateOf(listOf<Location>()) }

    DisposableEffect(Unit) {

        onDispose { systemUiController.setStatusBarColor(Color.White) }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
    ){
        /** 나중에 location 정보 받아올 곳 */
        NaverMap(
            locationSource = rememberFusedLocationSource(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onLocationChange = {
                /** 나중에 location 정보 받아올 곳 */
                if (locationList.isEmpty()){
                    /** 비어있으면 추가 */
                    locationList = locationList + it
                }else{
                    /** 거리비교해서 1M 이상 15M 이하면 추가 */
                    val distance = it.distanceTo(locationList.last())
                    if (distance >= 10f && distance <150f){
                        locationList = locationList + it
                        path = path + LatLng(it.latitude,it.longitude)
                    }
                }
                Log.d("LOG",locationList.toString())
            }
        ) {
            /** Path 를 그리는 컴포저블 **/
            if (path.size >= 2){
                PathOverlay(
                    coords = path,
                    outlineWidth = 1.dp,
                    color = Color(0xA0FFDBDB),
                    outlineColor = Color(0xA0FF5000),
                    width = 8.dp
                )
            }
            if (path.isNotEmpty()){
                //GroundOverlay(bounds = LatLng(path.first().latitude,path.first().longitude))
            }
        }
    }
}