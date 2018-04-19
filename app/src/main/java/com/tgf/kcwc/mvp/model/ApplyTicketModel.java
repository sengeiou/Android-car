package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/11/6
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyTicketModel {


    /**
     * count : 4
     * list : [{"id":375,"brand":"","car":"","user_mobile":"15723329439","user_nickname":"德玛西亚","series":"阿尔法-厂商2 2016"},{"id":373,"brand":"","car":"","user_mobile":"15223329437","user_nickname":"小^O^样","series":"一汽奥迪 A8L"},{"id":367,"brand":"阿尔法-厂商1","car":"","user_mobile":"15323326963","user_nickname":"153****6963","series":"G2-2018"},{"id":366,"brand":"阿尔法-厂商2","car":"","user_mobile":"15823568945","user_nickname":"158****8945","series":"2016"}]
     */

    public int count;
    public List<User> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        /**
         * id : 375
         * brand :
         * car :
         * user_mobile : 15723329439
         * user_nickname : 德玛西亚
         * series : 阿尔法-厂商2 2016
         */

        public int id;
        public String brand="";
        public String car="";
        @JsonProperty("user_mobile")
        public String mobile;
        @JsonProperty("user_nickname")
        public String nickname="";
        public String series="";
    }
}
