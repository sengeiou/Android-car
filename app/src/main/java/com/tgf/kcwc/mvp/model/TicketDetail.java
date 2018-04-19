package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/9 20:35
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketDetail extends Ticket {

    @JsonProperty("ticket_name")
    public String ticketName;

    @JsonProperty("event_name")
    public String senseName;

    @JsonProperty("event_cover")
    public String cover;
    @JsonProperty("haveContent")
    public int hasContent;

    @JsonProperty("ids")
    public List<Integer> ids;

    @JsonProperty("start_time")
    public String startTime;

    @JsonProperty("can_buy_num")
    public int canBeByNum;
    @JsonProperty("event_id")
    public int eventId;

    @JsonProperty("end_time")
    public String finishTime;

    public float spend;

    public float prevNum;

    @JsonProperty("can_get_nums")
    public int canGetNums;

    @JsonProperty("alpha")
    public String alpha;

}
