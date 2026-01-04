package com.sky.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MapConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class MapUtils {


    /**
     *   根据起点和终点  经纬度  获取 两点之间距离
     */
        public static double calculateDistance(String  startGeo,String endGeo) {
           double  startLng = Double.parseDouble(startGeo.split(",")[0]);
           double  startLat = Double.parseDouble(startGeo.split(",")[1]);
           double  endLng = Double.parseDouble(endGeo.split(",")[0]);
           double  endLat = Double.parseDouble(endGeo.split(",")[1]);

            String url = String.format("%s?key=%s&origins=%f,%f&destination=%f,%f", MapConstant.GAODE_DISTANCE_URL,
                    MapConstant.GAODE_AMAP_API_KEY, startLng, startLat, endLng, endLat);

            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpGet request = new HttpGet(url);
                HttpResponse response = httpClient.execute(request);
                String resultJson = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                JSONArray results = jsonObject.getJSONArray("results");
                if (results == null){
                    return 10000000.0;
                }
                JSONObject distanceInfo = results.getJSONObject(0);
                return Double.parseDouble(distanceInfo.get("distance").toString());
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }

    //2点之间的直线距离
    public static double calculateDistance2(String  startGeo,String endGeo) {
        double  startLng = Double.parseDouble(startGeo.split(",")[0]);
        double  startLat = Double.parseDouble(startGeo.split(",")[1]);
        double  endLng = Double.parseDouble(endGeo.split(",")[0]);
        double  endLat = Double.parseDouble(endGeo.split(",")[1]);

        String url = String.format("%s?key=%s&origins=%f,%f&destination=%f,%f", MapConstant.GAODE_DISTANCE_URL,
                MapConstant.GAODE_AMAP_API_KEY, startLng, startLat, endLng, endLat);
        url = url+"&type=0";

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String resultJson = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject distanceInfo = results.getJSONObject(0);
            String distanceStr = distanceInfo.get("distance").toString();
            double distance = Double.parseDouble(distanceStr);
            return distance;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


/**
 *     根基地理位置 解析对应的经纬度坐标
 */

      public static String  addressToLnglat(String address){
          String url = String.format("%s?key=%s&address=%s",
                  MapConstant.GAODE_GEO_URL, MapConstant.GAODE_AMAP_API_KEY,address);
          try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
              HttpGet request = new HttpGet(url);
              HttpResponse response = httpClient.execute(request);
              String resultJson = EntityUtils.toString(response.getEntity());
              JSONObject jsonObject = JSONObject.parseObject(resultJson);
              JSONArray results = jsonObject.getJSONArray("geocodes");
              if (results == null || results.size() < 1){
                  return null;
              }
              JSONObject jsonObject1 = results.getJSONObject(0);
              if(jsonObject1!=null){
                  Object location = results.getJSONObject(0).get("location");
                  return  location.toString();
              }
            return null;
          } catch (IOException e) {
              throw  new RuntimeException(e.getMessage());
          }
      }


    /**
     *    经纬度 得出地理位置
     * @param location  :经纬度
     * @return
     */


    public  static String  LngLatToAddress(String  location){
        String  url = "https://restapi.amap.com/v3/geocode/regeo?" +
                "output=JSON&location="+location+"&key="+
                MapConstant.GAODE_AMAP_API_KEY+"&extensions=all";
        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String resultJson = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            JSONObject results = jsonObject.getJSONObject("regeocode");
            String address = results.getString("formatted_address");
            return  address==null?"解析地址错误":address;
        } catch (IOException e) {
            throw  new RuntimeException(e.getMessage());
        }
    }


    /**
     *   坐标转换是一类简单的HTTP接口，能够将用户输入的非高德坐标（GPS坐标、mapbar坐标、baidu坐标）转换成高德坐标。
     * @param
     *   location  其他形式的坐标  :116.23128,40.22077
     *  coordsys :原坐标系，可选值：gps ;mapbar; baidu;   autonavi(不进行转换)
     * 默认autonavi
     * @return   转换为高德的坐标
     */
    public  static  String   otherLngLatChangeToGaodeLngLat(String location,String coordsys){
        String  url ="https://restapi.amap.com/v3/assistant/coordinate/convert?" +
                "locations="+location+"&coordsys="+coordsys+"&output=JSON&key="+MapConstant.GAODE_AMAP_API_KEY;
        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String resultJson = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            String locations = jsonObject.getString("locations");
            if(locations.contains(";")){
                String s = locations.split(";")[0];
                return s;
            }else{
             return locations;
            }
        } catch (IOException e) {
            throw  new RuntimeException(e.getMessage());
        }
    }

    /**
     * 坐标转换，腾讯地图转换成百度地图坐标
     * @param latitude 腾讯纬度
     * @param longitude 腾讯经度
     * @return 返回结果：经度,纬度
     */
    /**
     * 使用百度地图API计算骑行路线
     * @param originLat 起点纬度
     * @param originLng 起点经度
     * @param destinationAddress 终点地址
     * @return 路线信息，包含距离、预计时间和坐标点列表
     */
    public static Map<String, Object> calculateBicyclingRoute(double originLat, double originLng, String destinationAddress) {
        // 首先将终点地址转换为百度坐标
        String destinationLngLat = baiduAddressToLnglat(destinationAddress);
        if (destinationLngLat == null) {
            throw new RuntimeException("地址解析失败");
        }
        String[] destination = destinationLngLat.split(",");
        double destLat = Double.parseDouble(destination[0]);
        double destLng = Double.parseDouble(destination[1]);

        // 构建百度地图骑行路线规划API请求URL
        String url = String.format("%s?ak=%s&origin=%f,%f&destination=%f,%f&coord_type=wgs84ll",
                MapConstant.BAIDU_BICYCLING_ROUTE_PALN, MapConstant.BAIDU_AK_API_KEY,
                originLat, originLng, destLat, destLng);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String resultJson = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(resultJson);

            if (jsonObject.getInteger("status") != 0) {
                throw new RuntimeException("路线规划失败: " + jsonObject.getString("message"));
            }

            // 解析路线结果
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray routes = result.getJSONArray("routes");
            if (routes == null || routes.size() == 0) {
                throw new RuntimeException("未找到路线");
            }

            JSONObject route = routes.getJSONObject(0);
            int distance = route.getInteger("distance");
            int duration = route.getInteger("duration");

            // 解析路径坐标点
            JSONArray steps = route.getJSONArray("steps");
            List<Map<String, Double>> points = new ArrayList<>();

            for (int i = 0; i < steps.size(); i++) {
                JSONObject step = steps.getJSONObject(i);
                String path = step.getString("path");
                String[] pathPoints = path.split(";");
                
                for (String point : pathPoints) {
                    String[] lngLat = point.split(",");
                    Map<String, Double> pointMap = new HashMap<>();
                    pointMap.put("longitude", Double.parseDouble(lngLat[0]));
                    pointMap.put("latitude", Double.parseDouble(lngLat[1]));
                    points.add(pointMap);
                }
            }

            // 构建返回结果
            Map<String, Object> routeInfo = new HashMap<>();
            routeInfo.put("distance", distance + "米");
            routeInfo.put("duration", Math.round(duration / 60.0) + "分钟");
            routeInfo.put("points", points);
            routeInfo.put("destinationLatitude", destLat);
            routeInfo.put("destinationLongitude", destLng);

            return routeInfo;
        } catch (IOException e) {
            throw new RuntimeException("路线计算失败: " + e.getMessage());
        }
    }

    public static String map_tx2bd(double longitude, double latitude){
        double bd_lat;
        double bd_lon;
        double x_pi=3.14159265358979324;
        double x = longitude, y = latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        bd_lon = z * Math.cos(theta) + 0.0065;
        bd_lat = z * Math.sin(theta) + 0.006;

        System.out.println("bd_lat:"+bd_lat);
        System.out.println("bd_lon:"+bd_lon);
        return bd_lon+","+bd_lat;
    }

      public  static  String baiduAddressToLnglat(String address){
            String  params = "?address="+address+"&output=json&ak="+MapConstant.BAIDU_AK_API_KEY;
          try (
                  CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
              HttpGet request = new HttpGet(MapConstant.BAIDU_GEO_URL+params);
              HttpResponse response = httpClient.execute(request);
              String resultJson = EntityUtils.toString(response.getEntity());
              JSONObject jsonObject = JSONObject.parseObject(resultJson);
              JSONObject results = jsonObject.getJSONObject("result");
              Float lng = results.getJSONObject("location").getFloat("lng");
              Float lat = results.getJSONObject("location").getFloat("lat");
              return  lat+","+lng;
          } catch (IOException e) {
              throw  new RuntimeException(e.getMessage());
          }
      }

    /**
     *  计算两点之间的路程时间 : 单位 秒
     */

    public  static  Integer   baoiduTwoPointCostTime(String startAddress,String endAddress){
       Map map = new HashMap();
       map.put("ak",MapConstant.BAIDU_AK_API_KEY);
       map.put("origin",baiduAddressToLnglat(startAddress));
       map.put("destination",baiduAddressToLnglat(endAddress));
        String resultJSON = HttpClientUtil.doGet(MapConstant.BAIDU_BICYCLING_ROUTE_PALN, map);
        JSONArray routesArray = JSONObject.parseObject(resultJSON).getJSONObject("result").getJSONArray("routes");
        String duration = routesArray.getJSONObject(0).getString("duration");
        return  Integer.valueOf(duration);
    }

    /**
     *   cost 设置后可返回方案所需时间及费用成本
     *   polyline 设置后可返回分路段坐标点串，两点间用“,”分隔
     * @param startAddress
     * @param endAddress
     * @return
     */
// show_fields=cost,polyline
    public  static  Map<String,String>  gaodeTwoPointCostDistanceAndTime(String startAddress,String endAddress){
        Map map = new HashMap();
        map.put("key",MapConstant.GAODE_AMAP_API_KEY);
        map.put("origin",addressToLnglat(startAddress));
        map.put("destination",addressToLnglat(endAddress));
        map.put("show_fields","cost,polyline");
        String resultJSON = HttpClientUtil.doGet(MapConstant.GAODE_BICYCLING_ROUTE_PALN, map);
        JSONObject resultJsonObject = JSONObject.parseObject(resultJSON);
        JSONArray routesArray = resultJsonObject.getJSONObject("route").getJSONArray("paths");
        JSONArray  stepsArray = routesArray.getJSONObject(0).getJSONArray("steps");
        AtomicInteger index= new AtomicInteger();
        List<String[]> list = stepsArray.toJavaList(JSONObject.class).stream().map(item -> {
            String polyline = item.getString("polyline");
            String[] split = polyline.split(";"); //  [116.408689,40.058181,116.408689,40.058181]
            return split;
        }).collect(Collectors.toList());
        List list2 = new ArrayList();
        for (String[] strings : list) {
            for (int i = 0; i < strings.length; i++) {
                list2.add(strings[i]);
            }
        }
        System.out.println(list2);
        String distance = routesArray.getJSONObject(0).getString("distance");
        String duration = routesArray.getJSONObject(0).getString("duration");
        System.out.println("距离："+distance+"米"+" 总耗时："+duration+"秒");
        map.clear();
        map.put("duration",duration);
        map.put("distance",distance);
        return  map;
    }

    public  static  Map<String,String>  gaodeTwoPointCostDistanceAndTimeByAddress(String startAddress,String endAddress){
        Map map = new HashMap();
        map.put("key",MapConstant.GAODE_AMAP_API_KEY);
        map.put("origin",addressToLnglat(startAddress));
        map.put("destination",addressToLnglat(endAddress));
        map.put("show_fields","cost,polyline");
        String resultJSON = HttpClientUtil.doGet(MapConstant.GAODE_BICYCLING_ROUTE_PALN, map);
        JSONObject resultJsonObject = JSONObject.parseObject(resultJSON);
        JSONArray routesArray = resultJsonObject.getJSONObject("route").getJSONArray("paths");
        JSONArray  stepsArray = routesArray.getJSONObject(0).getJSONArray("steps");
        AtomicInteger index= new AtomicInteger();
        List<String[]> list = stepsArray.toJavaList(JSONObject.class).stream().map(item -> {
            String polyline = item.getString("polyline");
            String[] split = polyline.split(";"); //  [116.408689,40.058181,116.408689,40.058181]
            return split;
        }).collect(Collectors.toList());
        List list2 = new ArrayList();
        for (String[] strings : list) {
            for (int i = 0; i < strings.length; i++) {
                list2.add(strings[i]);
            }
        }
        System.out.println(list2);
        String distance = routesArray.getJSONObject(0).getString("distance");
        String duration = routesArray.getJSONObject(0).getString("duration");
        System.out.println("距离："+distance+"米"+" 总耗时："+duration+"秒");
        map.clear();
        map.put("duration",duration);
        map.put("distance",distance);
        return  map;
    }

    /**
     *   cost 设置后可返回方案所需时间及费用成本
     *   polyline 设置后可返回分路段坐标点串，两点间用“,”分隔
     *   坐标计算
     * @return
     */
// show_fields=cost,polyline
    public  static  Map<String,String>  gaodeTwoPointCostDistanceAndTimeByLngLat(String startLngLat,String endLngLat){
        Map map = new HashMap();
        map.put("key",MapConstant.GAODE_AMAP_API_KEY);
        map.put("origin",startLngLat);
        map.put("destination",endLngLat);
        map.put("show_fields","cost,polyline");
        String resultJSON = HttpClientUtil.doGet(MapConstant.GAODE_BICYCLING_ROUTE_PALN, map);
        JSONObject resultJsonObject = JSONObject.parseObject(resultJSON);
        JSONArray routesArray = resultJsonObject.getJSONObject("route").getJSONArray("paths");
        JSONArray  stepsArray = routesArray.getJSONObject(0).getJSONArray("steps");
        String distance = routesArray.getJSONObject(0).getString("distance");
        String duration = routesArray.getJSONObject(0).getString("duration");
        log.info("距离：{}",distance+"米"," 总耗时：{}",duration+"秒");
        map.clear();
        map.put("duration",duration);
        map.put("distance",distance);
        return  map;
    }

    /**
     *  获取配送费
     *  一公里之内是3元，超过一公里，每增加一公里加一元。
     * @param shoppAddressLngLat - 商家地址坐标
     * @param userAddressLngLat - 顾客地址坐标 经纬度
     * @return
     */
    public static BigDecimal getDelivoryCost(String shoppAddressLngLat, String userAddressLngLat) {
        BigDecimal cost=null;
        double  basicCost = 3.0;
        //  计算  快递员 提成费用：
        Map map = gaodeTwoPointCostDistanceAndTimeByLngLat(shoppAddressLngLat,userAddressLngLat);
        String distance  = (String)map.get("distance");
        Long disValue = Long.valueOf(distance);
        if(disValue<=1000){
            cost = new BigDecimal(basicCost); //  基础费用 3元
        }else{
            // (2000-1000)   1001
            long tax = (disValue - 1000) / 1000;
            long taxbit = (disValue - 1000) %1000;
            if(tax==0){
                //  1000---2000之内的距离    1670    费用：基础费用+1块
                cost = new BigDecimal(basicCost+1.0);
            }else {
                if(taxbit==0){
                    //  距离是超过1000米的整数
                    cost = new BigDecimal(basicCost+tax*1.0);
                }else{
                    //距离是超过1000米的非整数
                    //   大于2000米以上        费用 ： 基础费用+2.0+1.0
                    cost = new BigDecimal(basicCost+tax*1.0+1.0);
                }
            }
        }
        return  cost;
    }

    /**
     *  骑手配送费提成-经纬度计算
     * @param startLngLat
     * @param endLngLat
     * @return
     */
    public static BigDecimal getCourierCostByLngLat(String startLngLat, String endLngLat) {
        BigDecimal cost=null;
        BigDecimal courierCost=null;
        double  basicCost = 3.0; //  基本配送费
        //  计算  快递员 提成费用：
        Map map = gaodeTwoPointCostDistanceAndTimeByLngLat(startLngLat,endLngLat);
        String distance  = (String)map.get("distance");
        Long disValue = Long.valueOf(distance);
        if(disValue<=1000){
            cost = new BigDecimal(basicCost); //  基础费用 3元    50%费用
            courierCost = cost.multiply(new BigDecimal(0.5));
        }else{
            // (2000-1000)   1001
            long tax = (disValue - 1000) / 1000;
            long taxbit = (disValue - 1000) %1000;
            if(tax==0){
                //  1000---2000之内的距离    1670    费用：基础费用+1块
                cost = new BigDecimal(basicCost+1.0);
                courierCost = cost.multiply(new BigDecimal(0.6));
            }else {
                if(taxbit==0){
                    //  距离是超过1000米的整数
                    cost = new BigDecimal(basicCost+tax*1.0);
                    courierCost = cost.multiply(new BigDecimal(0.7));
                }else{
                    //距离是超过1000米的非整数
                    //   大于2000米以上        费用 ： 基础费用+2.0+1.0
                    cost = new BigDecimal(basicCost+tax*1.0+1.0);
                    courierCost = cost.multiply(new BigDecimal(0.75));
                }
            }
        }
        return  courierCost;
    }

    /**
     *  骑手配送费提成 ： 地址计算
     * @param
     *
     */
    public static BigDecimal getCourierCostByAddress(String startAddress, String endAddress) {
        BigDecimal cost=null;
        BigDecimal courierCost=null;
        double  basicCost = 3.0; //  基本配送费
        //  计算  快递员 提成费用：
        Map map = gaodeTwoPointCostDistanceAndTimeByAddress(startAddress,endAddress);
        String distance  = (String)map.get("distance");
        Long disValue = Long.valueOf(distance);
        if(disValue<=1000){
            cost = new BigDecimal(basicCost); //  基础费用 3元    50%费用
            courierCost = cost.multiply(new BigDecimal(0.5));
        }else{
            // (2000-1000)   1001
            long tax = (disValue - 1000) / 1000;
            long taxbit = (disValue - 1000) %1000;
            if(tax==0){
                //  1000---2000之内的距离    1670    费用：基础费用+1块
                cost = new BigDecimal(basicCost+1.0);
                courierCost = cost.multiply(new BigDecimal(0.6));
            }else {
                if(taxbit==0){
                    //  距离是超过1000米的整数
                    cost = new BigDecimal(basicCost+tax*1.0);
                    courierCost = cost.multiply(new BigDecimal(0.7));
                }else{
                    //距离是超过1000米的非整数
                    //   大于2000米以上        费用 ： 基础费用+2.0+1.0
                    cost = new BigDecimal(basicCost+tax*1.0+1.0);
                    courierCost = cost.multiply(new BigDecimal(0.75));
                }
            }
        }
        return  courierCost;
    }

    public static void main(String[] args) {
//        gaodeTwoPointCostDistanceAndTime("北京昌平区东小口镇中东录121号院", "北京昌平区建材城西路传智教育总部");
        String address = LngLatToAddress("116.338202,40.059225");
        System.out.println(address);
    }

}

