package cn.itcast.travel.service;

import cn.itcast.travel.domain.FavoritCount;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteService {

    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid, int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void add(String rid, int uid);

    void del(String rid, int uid);


    List<Route> searchfavoriteCount();
}
