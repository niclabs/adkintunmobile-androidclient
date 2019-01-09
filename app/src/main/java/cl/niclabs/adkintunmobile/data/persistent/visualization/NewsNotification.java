package cl.niclabs.adkintunmobile.data.persistent.visualization;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.commons.data.Persistent;

public class NewsNotification extends Persistent<NewsNotification>{

    /**
     * INFO: avisos locales, informativa de un comportamiento inusual
     * NEWS: avisos del servidor, noticias por broadcast
     * LOG: Registro de eventos locales
     */
    public final static int INFO = 1;
    public final static int NEWS = 2;
    public final static int SYNC_LOG = 3;

    @SerializedName("type")
    public Integer type;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("un_read")
    public boolean unRead;

    public NewsNotification() {
    }

    public NewsNotification(Integer type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.unRead = true;
    }

    static public long mostRecentlyTimestamp(){
        NewsNotification item = findFirst(
                NewsNotification.class,
                null,
                null,
                "timestamp DESC");

        return item == null ? 0 : item.timestamp;
    }

}
