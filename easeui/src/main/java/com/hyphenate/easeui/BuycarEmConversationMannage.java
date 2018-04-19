package com.hyphenate.easeui;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import static com.google.android.gms.internal.bd.f;

/**
 * Auther: Scott
 * Date: 2017/8/29 0029
 * E-mail:hekescott@qq.com
 */

public class BuycarEmConversationMannage {
    private static BuycarEmConversationMannage instace;
    private  List<EMMessage> messagesCache = new ArrayList<>();
    TreeMap<Long, EMMessage> sortedMessages = new TreeMap(new MessageComparator());
    private BuycarEmConversationMannage() {
    }

    public List<EMMessage> getMessagesCache() {
        ArrayList<EMMessage> var1 = new ArrayList();
        var1.addAll(this.sortedMessages.values());

        return var1;
    }

    public void setMessagesCache(List<EMMessage> messageList) {
        Iterator var2 = messageList.iterator();

        while(var2.hasNext()) {
            EMMessage var3 = (EMMessage)var2.next();
            addMessages(var3);
        }

        this.messagesCache.addAll(messageList);
    }
    public void addMessages(EMMessage var1) {
        if(var1 != null  && var1.getMsgTime() != 0L && var1.getMsgTime() != -1L && var1.getMsgId() != null && !var1.getMsgId().isEmpty()) {
//            String var2 = var1.getMsgId();
            sortedMessages.put(var1.getMsgTime(), var1);
//            messages.put(var2, var1);
//            this.idTimeMap.put(var2, Long.valueOf(var3));
        }
    }
    public void clearMessages() {
        this.messagesCache.clear();
        sortedMessages.clear();
    }
    public static BuycarEmConversationMannage getInstace() {
        if(instace==null){
            instace  = new BuycarEmConversationMannage();
        }
        return instace;
    }
    class MessageComparator implements Comparator<Long> {
        MessageComparator() {
        }

        public int compare(Long var1, Long var2) {
            long var3 = var1.longValue() - var2.longValue();
            return var3 > 0L?1:(var3 == 0L?0:-1);
        }
    }
}
