package com.tgf.kcwc.mvp.presenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.mvp.model.Pagination;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/11 20:00
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreCarListModel {

    /**
     * pagination : {"count":31,"page":1}
     * list : [{"factory_name":"上汽荣威威","oc_car_name":"","series_id":20,"icon_logo":"/moto/1701/20/43c36bbfdd486a1676137c8e6c581108.jpg","series_name":"朗逸","car_name":"","car_id":52,"appearance_color_name":"白色","interior_color_name":"黑色","organization":[{"id":9,"address":"重庆市高新区科园四路269号 ","org_name":"展览中心","oc_car_name":"","org_id":20,"car_id":52,"type_exist":0,"type_show":1,"update_by":0,"series_id":20,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/moto/1701/12/d94e1c47c3f34270cb016dda567930d0.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.492902","latitude":"29.525106","benefit":1,"distance":7064}],"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/moto/1701/12/d94e1c47c3f34270cb016dda567930d0.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"没有车型","series_id":15,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"奥迪A6L","car_name":"没有车型","car_id":999,"appearance_color_name":"白色","interior_color_name":"黑色","organization":[{"id":16,"address":"重庆市南岸区辅向路8号8栋（罗家坝妇幼保健院旁）","org_name":"重庆同捷-东风标致","oc_car_name":"没有车型","org_id":19,"car_id":999,"type_exist":0,"type_show":1,"update_by":0,"series_id":15,"appearance_img":"444.jpg","interior_img":"111.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.579446","latitude":"29.524291","benefit":0,"distance":5078}],"appearance_img":"444.jpg","interior_img":"111.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":15,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"奥迪A6L","car_name":"","car_id":1001,"appearance_color_name":"红色","interior_color_name":"黑色","organization":[{"id":27,"address":"科园4路口","org_name":"机构","oc_car_name":"","org_id":4,"car_id":1001,"type_exist":1,"type_show":1,"update_by":0,"series_id":15,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"红色","interior_color_name":"黑色","longitude":"106.592146","latitude":"29.568784","benefit":0,"distance":3975}],"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":15,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"奥迪A6L","car_name":"","car_id":1001,"appearance_color_name":"银色","interior_color_name":"黑色","organization":[{"id":47,"address":"重庆市高新区科园四路269号 ","org_name":"展览中心","oc_car_name":"","org_id":20,"car_id":1001,"type_exist":1,"type_show":0,"update_by":0,"series_id":15,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"银色","interior_color_name":"黑色","longitude":"106.492902","latitude":"29.525106","benefit":0,"distance":7064}],"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":15,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"奥迪A6L","car_name":"","car_id":1001,"appearance_color_name":"白色","interior_color_name":"黑色","organization":[{"id":18,"address":"重庆市南岸区辅向路8号8栋（罗家坝妇幼保健院旁）","org_name":"重庆同捷-东风标致","oc_car_name":"","org_id":19,"car_id":1001,"type_exist":1,"type_show":0,"update_by":0,"series_id":15,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.579446","latitude":"29.524291","benefit":0,"distance":5078},{"id":51,"address":"科园4路口","org_name":"机构","oc_car_name":"","org_id":4,"car_id":1001,"type_exist":0,"type_show":1,"update_by":0,"series_id":15,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.592146","latitude":"29.568784","benefit":0,"distance":3975}],"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":22,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"A4L","car_name":"","car_id":1007,"appearance_color_name":"银色","interior_color_name":"黑色","organization":[{"id":43,"address":"重庆市高新区科园四路269号 ","org_name":"展览中心","oc_car_name":"","org_id":20,"car_id":1007,"type_exist":1,"type_show":1,"update_by":0,"series_id":22,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"银色","interior_color_name":"黑色","longitude":"106.492902","latitude":"29.525106","benefit":0,"distance":7064}],"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":22,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"A4L","car_name":"","car_id":1007,"appearance_color_name":"红色","interior_color_name":"黑色","organization":[{"id":32,"address":"科园4路口","org_name":"机构","oc_car_name":"","org_id":4,"car_id":1007,"type_exist":1,"type_show":1,"update_by":0,"series_id":22,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"红色","interior_color_name":"黑色","longitude":"106.592146","latitude":"29.568784","benefit":0,"distance":3975}],"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":22,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"A4L","car_name":"","car_id":1007,"appearance_color_name":"白色","interior_color_name":"黑色","organization":[{"id":40,"address":"重庆电大","org_name":"tianlongbabu","oc_car_name":"","org_id":3,"car_id":1007,"type_exist":0,"type_show":1,"update_by":0,"series_id":22,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.493412","latitude":"29.52582","benefit":1,"distance":6977},{"id":56,"address":"科园4路口","org_name":"机构","oc_car_name":"","org_id":4,"car_id":1007,"type_exist":1,"type_show":1,"update_by":0,"series_id":22,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.592146","latitude":"29.568784","benefit":0,"distance":3975}],"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":23,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"A8L","car_name":"","car_id":37,"appearance_color_name":"白色","interior_color_name":"黑色","organization":[{"id":57,"address":"重庆电大","org_name":"tianlongbabu","oc_car_name":"","org_id":3,"car_id":37,"type_exist":0,"type_show":1,"update_by":0,"series_id":23,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.493412","latitude":"29.52582","benefit":0,"distance":6977}],"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"factory_name":"一汽奥迪","oc_car_name":"","series_id":23,"icon_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"A8L","car_name":"","car_id":1008,"appearance_color_name":"银色","interior_color_name":"黑色","organization":[{"id":44,"address":"重庆市高新区科园四路269号 ","org_name":"展览中心","oc_car_name":"","org_id":20,"car_id":1008,"type_exist":1,"type_show":0,"update_by":0,"series_id":23,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","appearance_color_name":"银色","interior_color_name":"黑色","longitude":"106.492902","latitude":"29.525106","benefit":0,"distance":7064}],"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg","interior_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"}]
     */

    public Pagination     pagination;
    public List<ListBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaginationBean {
        /**
         * count : 31
         * page : 1
         */

        public int count;
        public int page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListBean {
        /**
         * factory_name : 上汽荣威威
         * oc_car_name : 
         * series_id : 20
         * icon_logo : /moto/1701/20/43c36bbfdd486a1676137c8e6c581108.jpg
         * series_name : 朗逸
         * car_name : 
         * car_id : 52
         * appearance_color_name : 白色
         * interior_color_name : 黑色
         * organization : [{"id":9,"address":"重庆市高新区科园四路269号 ","org_name":"展览中心","oc_car_name":"","org_id":20,"car_id":52,"type_exist":0,"type_show":1,"update_by":0,"series_id":20,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","interior_img":"/moto/1701/12/d94e1c47c3f34270cb016dda567930d0.jpg","appearance_color_name":"白色","interior_color_name":"黑色","longitude":"106.492902","latitude":"29.525106","benefit":1,"distance":7064}]
         * appearance_img : /car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg
         * interior_img : /moto/1701/12/d94e1c47c3f34270cb016dda567930d0.jpg
         */

        @JsonProperty("factory_name")
        public String                 factoryName;
        @JsonProperty("oc_car_name")
        public String                 ocCarName;
        @JsonProperty("series_id")
        public int                    seriesId;
        public String                 logo;
        @JsonProperty("series_name")
        public String                 seriesName;
        @JsonProperty("car_name")
        public String                 carName;
        @JsonProperty("car_id")
        public int                    carId;
        @JsonProperty("appearance_color_name")
        public String                 appearanceColorName;
        @JsonProperty("interior_color_name")
        public String                 interiorColorName;
        @JsonProperty("appearance_img")
        public String                 appearanceImg;
        @JsonProperty("interior_img")
        public String                 interiorImg;
        @JsonProperty("is_series")
        public int                    isSeries;
        public List<OrganizationBean> organization;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class OrganizationBean {
            /**
             * id : 9
             * address : 重庆市高新区科园四路269号 
             * org_name : 展览中心
             * oc_car_name : 
             * org_id : 20
             * car_id : 52
             * type_exist : 0
             * type_show : 1
             * update_by : 0
             * series_id : 20
             * appearance_img : /car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg
             * interior_img : /moto/1701/12/d94e1c47c3f34270cb016dda567930d0.jpg
             * appearance_color_name : 白色
             * interior_color_name : 黑色
             * longitude : 106.492902
             * latitude : 29.525106
             * benefit : 1
             * distance : 7064
             */

            public int    id;
            public String address;
            @JsonProperty("org_name")
            public String orgName;
            @JsonProperty("oc_car_name")
            public String ocCarName;
            @JsonProperty("org_id")
            public int    orgId;
            @JsonProperty("car_id")
            public int    carId;
            @JsonProperty("type_exist")
            public int    typeExist;
            @JsonProperty("type_show")
            public int    typeShow;
            @JsonProperty("update_by")
            public int    updateBy;
            @JsonProperty("series_id")
            public int    seriesId;
            @JsonProperty("appearance_img")
            public String appearanceImg;
            @JsonProperty("interior_img")
            public String interiorImg;
            @JsonProperty("appearance_color_name")
            public String appearanceColorName;
            @JsonProperty("interior_color_name")
            public String interiorColorName;
            public String longitude;
            public String latitude;
            public int    benefit;
            public int    distance;
        }
    }
}
